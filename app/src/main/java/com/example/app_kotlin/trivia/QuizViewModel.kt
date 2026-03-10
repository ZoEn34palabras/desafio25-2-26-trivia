package com.example.app_kotlin.trivia

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizUiState(
            questions = seedQuestions()
        )
    )

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onSelectedOption(index: Int) {
        val current = _uiState.value

        if (current.isFinished) return
        if (current.showFeedback) return

        _uiState.value = current.copy(selectedIndex = index)
    }

    fun onConfirmAnswer() {
        val current = _uiState.value

        if (current.isFinished) return

        // First click: evaluate answer and show feedback
        if (!current.showFeedback) {
            val selected = current.selectedIndex ?: return
            val currentQuestion = current.currentQuestion ?: return

            val isCorrect = selected == currentQuestion.correctIndex
            val newScore = if (isCorrect) current.score + 100 else current.score

            _uiState.value = current.copy(
                score = newScore,
                showFeedback = true,
                isAnswerCorrect = isCorrect
            )
            return
        }

        // Second click: move to next question or finish
        val nextIndex = current.currentIndex + 1
        val finished = nextIndex >= current.questions.size

        _uiState.value = current.copy(
            currentIndex = nextIndex,
            selectedIndex = null,
            isFinished = finished,
            showFeedback = false,
            isAnswerCorrect = null
        )
    }

    private fun seedQuestions(): List<Question> {
        return listOf(
            Question(
                id = 1,
                title = "¿Qué palabra clave se usa para declarar una variable inmutable en Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctIndex = 1
            ),
            Question(
                id = 2,
                title = "En Jetpack Compose, ¿qué anotación marca una función como UI?",
                options = listOf("@UI", "@Widget", "@Composable", "@Compose"),
                correctIndex = 2
            ),
            Question(
                id = 3,
                title = "¿Qué componente se usa para listas eficientes y scrolleables?",
                options = listOf("Column", "RecyclerView", "Stack", "LazyColumn"),
                correctIndex = 3
            ),
            Question(
                id = 4,
                title = "¿Qué objeto contiene los datos para restaurar el estado de una Activity?",
                options = listOf("intentData", "savedInstanceState", "activityState", "bundleConfig"),
                correctIndex = 1
            ),
            Question(
                id = 5,
                title = "¿Qué función se usa normalmente para mostrar contenido Compose en una Activity?",
                options = listOf("setView", "setScreen", "setContent", "showContent"),
                correctIndex = 2
            ),
            Question(
                id = 6,
                title = "¿Cuál de estas opciones permite observar estado en Compose?",
                options = listOf("remember", "collect", "launch", "Bundle"),
                correctIndex = 0
            )
        )
    }
}