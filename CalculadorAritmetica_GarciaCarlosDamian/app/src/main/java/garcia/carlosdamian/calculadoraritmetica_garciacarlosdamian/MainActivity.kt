package garcia.carlosdamian.calculadoraritmetica_garciacarlosdamian

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Usamos lateinit para evitar los "!!"
    private lateinit var tvInput: TextView

    // Variables para controlar el estado de la entrada
    private var esPunto = false
    private var ultimoEsNumero = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)

        // Asignar el listener a todos los botones numéricos y de punto
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnPunto
        )
        buttonIds.forEach { findViewById<Button>(it).setOnClickListener(this::onNumberClick) }

        // Asignar listener a los botones de operaciones
        val operatorIds = listOf(
            R.id.btnSumar, R.id.btnRestar, R.id.btnMultiplicar, R.id.btnDividir, R.id.btnPorcentaje
        )
        operatorIds.forEach { findViewById<Button>(it).setOnClickListener(this::onOperatorClick) }

        // Listeners para botones especiales
        findViewById<Button>(R.id.btnAC).setOnClickListener { onClear() }
        findViewById<Button>(R.id.btnIgual).setOnClickListener { onEqual() }
        findViewById<Button>(R.id.btnParentesis).setOnClickListener { onParenthesisClick() }
    }

    // Este método ya no es necesario, lo dividimos en funciones más específicas
    override fun onClick(v: View?) {}

    private fun onNumberClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        if (buttonText == ".") {
            if (ultimoEsNumero && !esPunto) {
                tvInput.append(buttonText)
                esPunto = true
                ultimoEsNumero = false // Después de un punto no se puede poner otro punto
            }
        } else {
            tvInput.append(buttonText)
            ultimoEsNumero = true
        }
    }

    private fun onOperatorClick(view: View) {
        val button = view as Button
        if (ultimoEsNumero && tvInput.text.isNotEmpty()) {
            tvInput.append(button.text)
            ultimoEsNumero = false
            esPunto = false
        }
    }

    private fun onParenthesisClick() {
        val text = tvInput.text.toString()
        val openParentheses = text.count { it == '(' }
        val closedParentheses = text.count { it == ')' }

        if (ultimoEsNumero) {
            if (openParentheses > closedParentheses) {
                tvInput.append(")")
                ultimoEsNumero = false
            }
        } else {
            tvInput.append("(")
            ultimoEsNumero = false
        }
    }


    private fun onClear() {
        tvInput.text = ""
        ultimoEsNumero = false
        esPunto = false
    }

    private fun onEqual() {
        val expresionStr = tvInput.text.toString()
        if (expresionStr.isEmpty() || !ultimoEsNumero) {
            return
        }

        try {
            val expresionParaCalcular = expresionStr.replace("%", "/100")
            val expression = ExpressionBuilder(expresionParaCalcular).build()
            val resultado = expression.evaluate()

            // Si el resultado es un entero, mostrarlo sin decimales
            if (resultado == resultado.toLong().toDouble()) {
                tvInput.text = resultado.toLong().toString()
            } else {
                tvInput.text = resultado.toString()
            }

            esPunto = tvInput.text.contains(".")
        } catch (e: Exception) {
            Toast.makeText(this, "Expresión inválida", Toast.LENGTH_SHORT).show()
        }
    }
}