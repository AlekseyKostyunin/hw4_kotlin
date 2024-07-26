class JsonObject {
    private val map = mutableMapOf<String, Any>()

    fun addProperty(key: String, value: Any) {
        map[key] = value
    }

    override fun toString(): String {
        val properties = map.entries.joinToString(",\n    ") { (key, value) ->
            "\"$key\": ${if (value is String) "\"$value\"" else value}"
        }
        return "{\n    $properties\n}"
    }
}