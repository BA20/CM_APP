package ipvc.estg.plantme.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.ml.Model
import ipvc.estg.plantme.util.YuvToRgbConverter
import org.tensorflow.lite.support.image.TensorImage
import java.util.concurrent.Executors

// Número de resultados a apresentar na recycler view
private const val MAX_RESULT_DISPLAY = 3
// Variáveis relacionadas com as permissões da câmera
private const val REQUEST_CODE_PERMISSIONS = 999
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


typealias RecognitionListener = (recognition: List<Recognition>) -> Unit

class CameraActivity : AppCompatActivity() {

    // Preview para apresentar a câmera
    private lateinit var preview: Preview // Preview use case, fast, responsive view of the camera
    // Utilização da classe ImageAnalyis para fornecer as imagens ao modelo
    private lateinit var imageAnalyzer: ImageAnalysis

    //Variáveis para a câmera e a sua execução
    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    // Obtenção da view onde se vai apresentar os resultados da análise
    private val resultRecyclerView by lazy {
        this.findViewById<RecyclerView>(R.id.recognitionResults)
    }

    // Obtenção da view que vai apresentar o vídeo da câmera
    private val viewFinder by lazy {
        this.findViewById<PreviewView>(R.id.viewFinder)
    }

    // View Model que contém os resultados do reconhecimento
    private val recogViewModel: CameraListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Obtenção das permissões para a utilização da câmera
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Inicialização da RecyclerView de resultados e do seu adaptador
        val viewAdapter = ResultadosAdapter(this)
        resultRecyclerView.adapter = viewAdapter
        resultRecyclerView.itemAnimator = null

        //Atualização dos resultados da lista quando existir alterações
        recogViewModel.recognitionList.observe(this,
                Observer {
                    viewAdapter.submitList(it)
                }
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onBackPressed() {
        val replyIntent = Intent()
        setResult(Activity.RESULT_CANCELED, replyIntent)
        finish()
    }

    //Obtenção das permissões
    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Verificação do resultado da obtenção das permissões pelo utilizador
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                        this,
                        getString(R.string.permission_deny_text),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                    .build()
            imageAnalyzer = ImageAnalysis.Builder()
                    //Definição do tamanho da imagem, para que seja o ideal ao modelo de IA criado
                    .setTargetResolution(Size(224, 224))
                    // Estratégia de obtenção da imagem, neste caso obtendo o frame mais recente
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysisUseCase: ImageAnalysis ->
                        //Atualização dos itens da recycler view
                        analysisUseCase.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                            recogViewModel.updateData(items)
                        })
                    }
            // Selecionar a câmara de trás, caso contrário a de frente
            val cameraSelector =
                    if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                        CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                // Bind da câmera ao ciclo de vida da atividade, utilizando o preview e o image analyzer criados
                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                )

                // Definir a apresentação na view do tipo Preview
                preview.setSurfaceProvider(viewFinder?.surfaceProvider)
            } catch (exc: Exception) {
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {

        // Inicialização do modelo
        private val doencasPlantas = Model.newInstance(ctx)

        override fun analyze(imageProxy: ImageProxy) {
            val items = mutableListOf<Recognition>()

            // Conversão de Bitmap para TensorImage
            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))

            // Processamento da imagem, ordenando pelo maior grau de confiança, obtendo os
            // primeiros três elementos (definidos na variável, topo da classe)
            val outputs = doencasPlantas.process(tfImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score } // Sort with highest confidence first
                }.take(MAX_RESULT_DISPLAY)

            // Conversão dos itens numa lista de objetos Recognition
            for (output in outputs) {
                items.add(Recognition(output.label, output.score))
            }

            // Retornar os resultados, atualizando a recycler view de resultados
            listener(items.toList())

            // Fecho da imagem, para que a próxima possa ser processada
            imageProxy.close()
        }

        /**
         * Convert Image Proxy to Bitmap
         */
        //CONVERSÃO DE IMAGE PROXY PARA BITMAP, UTILIZANDO A CLASSE
        //UTILITÁRIA: YuvToRgbConverter
        private val yuvToRgbConverter = YuvToRgbConverter(ctx)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeExperimentalUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {

            val image = imageProxy.image ?: return null

            if (!::bitmapBuffer.isInitialized) {
                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                        imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }

            // Conversão da imagem, passando-a para a classe
            yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)

            // Criação do bitmap com a mesma rotação
            return Bitmap.createBitmap(
                    bitmapBuffer,
                    0,
                    0,
                    bitmapBuffer.width,
                    bitmapBuffer.height,
                    rotationMatrix,
                    false
            )
        }

    }
}