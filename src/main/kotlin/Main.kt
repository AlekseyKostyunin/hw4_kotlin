import java.io.File

fun main() {
    println(comandsStartMenu)
    while (true) {
        val command = readCommand(readLine()!!.lowercase())
        println(command)
        if (command.isValid()) {
            when (command) {
                is ExportCommand -> {
                    val jsonObjects = phoneBook.values.map { person ->
                        json {
                            addProperty("name", person.name)
                            addProperty("phones", person.phones)
                            addProperty("emails", person.emails)
                        }
                    }
                    val json = "[${jsonObjects.joinToString(", ")}]"
                    File(command.path).writeText(json)
                    println("Данные экспортированы в файл ${command.path}")
                }

                is AddPhoneCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.phones.add(command.phone)
                    println("Добавлено: ${it.name}, телефон: ${command.phone}")
                }

                is AddEmailCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.emails.add(command.email)
                    println("Добавлено: ${it.name}, email: ${command.email}")
                }

                is ShowCommand -> {
                    phoneBook[command.name]?.let {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Запись не найдена")
                }

                is FindCommand -> {
                    phoneBook.values.filter {
                        it.phones.contains(command.info)
                                || it.emails.contains(command.info)
                    }.takeIf { it.isNotEmpty() }?.forEach {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Записи не найдены")
                }

                is HelpCommand -> {
                    println(comandsStartMenu)
                }

                is ExitCommand -> return
            }
        } else {
            println("Неверный формат команды, попробуйте еще раз")
        }
    }
}

val phoneBook = mutableMapOf<String, Person>()

// Функция для чтения команды пользователя из консоли
fun readCommand(input: String): Command {
    val parts = input.split(" ")
    return when {
        parts[0] == "exit" -> ExitCommand
        parts[0] == "help" -> HelpCommand
        parts.size == 2 -> when (parts[0]) {
            "show" -> ShowCommand(parts[1])
            "find" -> FindCommand(parts[1])
            "export" -> ExportCommand(parts[1])
            else -> HelpCommand
        }

        parts.size == 4 && parts[0] == "add" -> when (parts[2]) {
            "phone" -> AddPhoneCommand(parts[1], parts[3])
            "email" -> AddEmailCommand(parts[1], parts[3])
            else -> HelpCommand
        }

        else -> HelpCommand
    }
}

fun json(init: JsonObject.() -> Unit): JsonObject {
    return JsonObject().apply(init)
}

private val comandsStartMenu = "Введите одну из команд:\n" +
        "1. exit\n" +
        "2. help\n" +
        "3. add <Имя> phone <Номер телефона>\n" +
        "4. add <Имя> email <Адрес электронной почты>\n" +
        "5. show <Имя>\n" +
        "6. find <Телефон или Email>\n" +
        "7. export <Путь к файлу .json>"