package com.pp272cs388.wordle

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.jin616.confetti.ConfettiView


class MainActivity : AppCompatActivity() {

    object FourLetterWordList {
        private val listOne = "Area,Army,Baby,Back".split(",") // Sample words
        private val listTwo = "Gold,Hair,Hall,Hand".split(",") // Sample words
        private val listThree = "Note,Pain,Park,Past".split(",") // Sample words

        private var currentWordList: List<String> = listOne

        fun setWordList(listName: String) {
            currentWordList = when (listName) {
                "List Two" -> listTwo
                "List Three" -> listThree
                else -> listOne
            }
        }

        fun getRandomFourLetterWord(): String {
            return currentWordList.random().uppercase()
        }
    }

    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var itemList = mutableListOf<String>()
    private var wordToGuess = ""
    private var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // UI components
        val resultText: ListView = findViewById(R.id.result_text)
        val submitButton: Button = findViewById(R.id.submit_button)
        val inputWord: EditText = findViewById(R.id.input_word)
        val finalResult: TextView = findViewById(R.id.final_result)
        val wordListSpinner: Spinner = findViewById(R.id.word_list_spinner)

        // Adapter for ListView
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        resultText.adapter = arrayAdapter

        // Set up spinner
        val wordLists = listOf("List One", "List Two", "List Three")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wordLists)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        wordListSpinner.adapter = spinnerAdapter

        // Spinner listener to handle list changes
        wordListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                FourLetterWordList.setWordList(wordLists[position])
                wordToGuess = FourLetterWordList.getRandomFourLetterWord()
                resetGame()  // Ensure game is reset when switching lists
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Initialize the first word
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()

        val confettiView: ConfettiView = findViewById(R.id.confetti_view)

        // Submit button logic
        submitButton.setOnClickListener {
            val guess = inputWord.text.toString().uppercase()

            if (guess.length != 4) {
                Toast.makeText(this, "Enter a 4-letter word", Toast.LENGTH_SHORT).show()
            } else {
                addItemToList("Guess #$counter - $guess")

                val result = checkGuess(guess)
                addItemToList("Guess #$counter Check - $result")

                if (guess == wordToGuess) {
                    confettiView.visibility = View.VISIBLE
                    confettiView.startConfetti()

                    Toast.makeText(this, "You Won!", Toast.LENGTH_LONG).show()
                    finalResult.text = "Correct Word: $wordToGuess"
                    inputWord.visibility = View.INVISIBLE
                    submitButton.visibility = View.INVISIBLE
                } else if (counter >= 3) {
                    Toast.makeText(this, "You Lost!", Toast.LENGTH_LONG).show()
                    finalResult.text = "Correct Word: $wordToGuess"
                    inputWord.visibility = View.INVISIBLE
                    submitButton.visibility = View.INVISIBLE
                }

                counter++
            }

            inputWord.text.clear()
        }
    }

    // Helper method to check guesses and return colored result
    private fun checkGuess(guess: String): String {
        val result = StringBuilder()
        val spannableResult = SpannableString(guess)

        for (i in guess.indices) {
            when {
                guess[i] == wordToGuess[i] -> { // Correct position
                    result.append("O")
                    spannableResult.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.correct)),
                        i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                guess[i] in wordToGuess -> { // Wrong position
                    result.append("+")
                    spannableResult.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.misplaced)),
                        i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                else -> { // Incorrect letter
                    result.append("X")
                    spannableResult.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.incorrect)),
                        i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        // Update the UI with the colored result
        findViewById<TextView>(R.id.result).text = spannableResult
        return result.toString()
    }

    // Reset game state when the word list changes
    private fun resetGame() {
        itemList.clear()  // Clear old items in ListView
        arrayAdapter.notifyDataSetChanged()  // Notify adapter to refresh the ListView

        findViewById<TextView>(R.id.final_result).text = ""
        findViewById<EditText>(R.id.input_word).visibility = View.VISIBLE
        findViewById<Button>(R.id.submit_button).visibility = View.VISIBLE
        counter = 1
    }

    // Add an item to the list and refresh the adapter
    private fun addItemToList(item: String) {
        itemList.add(item)
        arrayAdapter.notifyDataSetChanged()
    }
}
