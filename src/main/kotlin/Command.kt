sealed interface Command {
    fun isValid(): Boolean
}

data class AddPhoneCommand(val name: String, val phone: String) : Command {
    override fun isValid() = phone.matches(Regex("\\+?\\d+")) // Проверка валидности номера телефона
    override fun toString(): String {
        return "Вызвана команда добавления с указанием телефона"
    }
}

data class AddEmailCommand(val name: String, val email: String) : Command {
    override fun isValid() = email.matches(Regex("\\w+@\\w+\\.\\w+")) // Проверка валидности email
    override fun toString(): String {
        return "Вызвана команда добавления с указанием почты"
    }
}

object ExitCommand : Command {
    override fun isValid() = true
}

object HelpCommand : Command {
    override fun isValid() = true
}

// Команда показа информации о пользователе по имени
data class ShowCommand(val name: String) : Command {
    override fun isValid() = true
}

// Команда поиска данных по телефону или почте
data class FindCommand(val info: String) : Command {
    override fun isValid() = true
}