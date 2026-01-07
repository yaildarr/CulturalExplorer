package ru.ildar.network.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import ru.ildar.network.dto.DescriptionDto

class DescriptionTypeAdapter : TypeAdapter<DescriptionDto>() {

    override fun write(out: JsonWriter, value: DescriptionDto?) {
        when (value) {
            is DescriptionDto.Text -> out.value(value.value)
            is DescriptionDto.Raw -> out.value(value.text)
            null -> out.nullValue()
        }
    }

    override fun read(reader: JsonReader): DescriptionDto? {
        return when (reader.peek()) {
            JsonToken.STRING -> {
                DescriptionDto.Raw(reader.nextString())
            }

            JsonToken.BEGIN_OBJECT -> {
                reader.beginObject()
                var value: String? = null
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "value" -> value = reader.nextString()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                DescriptionDto.Text(value.orEmpty())
            }

            JsonToken.NULL -> {
                reader.nextNull()
                null
            }

            else -> {
                reader.skipValue()
                null
            }
        }
    }
}
