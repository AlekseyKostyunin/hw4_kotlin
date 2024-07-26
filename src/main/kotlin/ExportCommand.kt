data class ExportCommand(val path: String) : Command {
    override fun isValid() = true
}