package garcia.carlosdamian.calculadoraritmetica_garciacarlosdamian

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var tvInput: TextView? = null
    private var esPunto = false
    private var esParentesisAbierto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)

        // Asignar el listener a todos los botones
        findViewById<View>(R.id.btn0).setOnClickListener(this)
        findViewById<View>(R.id.btn1).setOnClickListener(this)
        findViewById<View>(R.id.btn2).setOnClickListener(this)
        findViewById<View>(R.id.btn3).setOnClickListener(this)
        findViewById<View>(R.id.btn4).setOnClickListener(this)
        findViewById<View>(R.id.btn5).setOnClickListener(this)
        findViewById<View>(R.id.btn6).setOnClickListener(this)
        findViewById<View>(R.id.btn7).setOnClickListener(this)
        findViewById<View>(R.id.btn8).setOnClickListener(this)
        findViewById<View>(R.id.btn9).setOnClickListener(this)
        findViewById<View>(R.id.btnPunto).setOnClickListener(this)

        findViewById<View>(R.id.btnSumar).setOnClickListener(this)
        findViewById<View>(R.id.btnRestar).setOnClickListener(this)
        findViewById<View>(R.id.btnMultiplicar).setOnClickListener(this)
        findViewById<View>(R.id.btnDividir).setOnClickListener(this)
        findViewById<View>(R.id.btnPorcentaje).setOnClickListener(this)
        findViewById<View>(R.id.btnParentesis).setOnClickListener(this)

        findViewById<View>(R.id.btnAC).setOnClickListener(this)
        findViewById<View>(R.id.btnIgual).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        val b = v as Button
        val buttonText = b.text.toString()
        val textoActual = tvInput!!.text.toString()

        if (id == R.id.btnAC) {
            tvInput!!.text = ""
            esPunto = false
        } else if (id == R.id.btnIgual) {
            calcularResultado(textoActual)
        } else if (id == R.id.btnParentesis) {
            manejarParentesis()
        } else if (buttonText == ".") {
            if (!esPunto) {
                tvInput!!.append(buttonText)
                esPunto = true
            }
        } else if (id == R.id.btnSumar || id == R.id.btnRestar || id == R.id.btnMultiplicar || id == R.id.btnDividir || id == R.id.btnPorcentaje) {
            tvInput!!.append(buttonText)
            esPunto = false // Se puede poner punto después de un operador
        } else {
            tvInput!!.append(buttonText)
        }
    }

    private fun manejarParentesis() {
        if (esParentesisAbierto) {
            tvInput!!.append(")")
            esParentesisAbierto = false
        } else {
            tvInput!!.append("(")
            esParentesisAbierto = true
        }
    }

    private fun calcularResultado(expresionStr: String) {
        if (expresionStr.isEmpty()) {
            return
        }

        try {
            // Reemplazar el símbolo de porcentaje si es necesario para el cálculo
            val expresionParaCalcular = expresionStr.replace("%", "/100")

            val expression: Expression = ExpressionBuilder(expresionParaCalcular).build()
            val resultado: Double = expression.evaluate()

            // Si el resultado es un entero, mostrarlo sin decimales
            if (resultado == resultado.toLong().toDouble()) {
                tvInput!!.text = String.format("%d", resultado.toLong())
            } else {
                tvInput!!.text = String.format("%s", resultado)
            }

            // Permitir un nuevo punto en el resultado si es decimal
            esPunto = tvInput!!.text.toString().contains(".")
        } catch (e: Exception) {
            Toast.makeText(this, "Expresión inválida", Toast.LENGTH_SHORT).show()
        }
    }
}