package com.example.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * "Pilha" responsável pela equação montada. Ex: [6.5, '+', 5.5]
     */
    private var stack: MutableList<Any> = mutableListOf()

    /**
     * Equação a ser exibida na tela
     */
    private var equation: String = ""

    /**
     * Número atual sendo lido
     */
    private var actualNumber: String = ""

    /**
     * Contador de pontos enquanto um número está sendo lido.
     */
    private var dotCount: Int = 0

    /**
     * Flag para definir quando ler uma nova equação.
     */
    private var isNewEquation: Boolean = false

    /**
     * Guarda o valor do ultimo resultado
     */
    private var lastResult: Float = Float.NaN

    private val dialogTitle: String = "Operação inválida"
    private val dialogMessage: String = "Por favor digite uma operação válida."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") {_, _->
            // limpa a pilha e mantem o resultado salvo
            stack = mutableListOf()
//            clear()
        }

        // Definição dos eventos de click dos números e pontos
        btn_calc_dot.setOnClickListener { concatNumber(".") }
        btn_calc_0.setOnClickListener { concatNumber("0") }
        btn_calc_1.setOnClickListener { concatNumber("1") }
        btn_calc_2.setOnClickListener { concatNumber("2") }
        btn_calc_3.setOnClickListener { concatNumber("3") }
        btn_calc_4.setOnClickListener { concatNumber("4") }
        btn_calc_5.setOnClickListener { concatNumber("5") }
        btn_calc_6.setOnClickListener { concatNumber("6") }
        btn_calc_7.setOnClickListener { concatNumber("7") }
        btn_calc_8.setOnClickListener { concatNumber("8") }
        btn_calc_9.setOnClickListener { concatNumber("9") }

        // Definição dos eventos de operadores
        btn_add.setOnClickListener { concatOperator('+') }
        btn_div.setOnClickListener { concatOperator('/') }
        btn_mul.setOnClickListener { concatOperator('*') }
        btn_sub.setOnClickListener { concatOperator('-') }

        // Definição do evento de clear
        btn_clear.setOnClickListener {
            clear()
        }

        // Definição do evente de igual
        btn_calc_equals.setOnClickListener {
            if (actualNumber.isEmpty()) { // exibe erro caso o numero atual esteja vazio
                builder.show()
            } else if(dotCount > 1) { // exibe erro caso exista um número com mais de 1 ponto
                builder.show()
                clear()
            } else {
                dotCount = 0
                stack.add(actualNumber.toFloat())

                if (stack.size < 3 || stack[0] !is Float || stack[1] !is Char || stack[2] !is Float) { // exibe erro caso o padrão [valor] [operador] [valor] não seja atendido
                    builder.show()
                } else {

                    // Obtém os valores da "Pilha"
                    val first = stack[0] as Float
                    val second = stack[2] as Float
                    val answer = when (stack[1]) {
                        '/' -> first / second
                        '*' -> first * second
                        '-' -> first - second
                        '+' -> first + second
                        else -> null
                    }

                    // Realiza a operação guardando o resultado
                    if (answer is Float) {
                        lastResult = answer
                        text_result.text = answer.toString()
                        stack = mutableListOf()
                        actualNumber = ""
                    }
                }
            }
        }

    }

    /**
     * Concatena o número à equação sendo exibida.
     */
    private fun concatNumber(number: String) {

        // Limpa a equação atual caso tenha um resultado salvo e um número é inserido.
        if (!lastResult.equals(Float.NaN)) {
            clear()
        }

        if (number === ".")
            dotCount++

        // Apenas concatena caso seja um caracter válido
        if (number !== "." || dotCount < 2)
            if (isNewEquation) {
                equation = ""
                isNewEquation = false
            }
            actualNumber = "$actualNumber$number"
            equation = "$equation$number"
            text_result.text = equation
    }

    /**
     * Concatena o operador à equação sendo exibida.
     * Representa, o final da leitura de um número, inserindo-o à "Pilha" de equação.
     */
    private fun concatOperator(character: Char) {
        // Caso o último resultado exista, o define como número atual e mantem na equação.
        if (!lastResult.equals(Float.NaN)) {
            actualNumber = lastResult.toString()
            lastResult = Float.NaN
            equation = actualNumber
        }

        if (isNewEquation) {
            equation = ""
            isNewEquation = false
        }

        // Caso número atual existir, o adiciona na pilha e adiciona também o operador passado por parâmetro.
        if (actualNumber.isNotEmpty())
            stack.add(actualNumber?.toFloat())
            stack.add(character)
            actualNumber = ""
            dotCount = 0

        // Atualiza a equação
        equation = "$equation$character"
        text_result.text = equation
    }

    /**
     * Realiza a limpeza total das variáveis, reiniciando a calculadora.
     */
    private fun clear() {
        equation = ""
        isNewEquation = true
        text_result.text = equation
        stack = mutableListOf()
        actualNumber = ""
        lastResult = Float.NaN
    }
}

