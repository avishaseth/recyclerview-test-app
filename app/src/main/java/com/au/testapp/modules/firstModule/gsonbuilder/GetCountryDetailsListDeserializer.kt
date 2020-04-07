package com.au.testapp.modules.firstModule.gsonbuilder

import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.model.Row
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

/* Parse the data for the details of the country list API */
class GetCountryDetailsListDeserializer : JsonDeserializer<Results> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Results {
        val results = Results()
        val jsonObject = json.asJsonObject
        val about = jsonObject.get("title").asString
        results.title = about
        val itemsJsonArray = jsonObject.getAsJsonArray("rows")
        if (itemsJsonArray == null || itemsJsonArray.size() == 0) {
            return results
        }
        var rows: MutableList<Row>? = null
        if (itemsJsonArray.size() > 0) {
            rows = ArrayList()
            results.rows = rows
        }

        for (itemsJsonElement in itemsJsonArray) {
            val itemJsonObject = itemsJsonElement.asJsonObject
            val row = Row()
            val titleObject = itemJsonObject.get("title")
            if (titleObject != null && !titleObject.isJsonNull) {
                val title = titleObject.asString
                row.title = title
            }

            val descriptionObject = itemJsonObject.get("description")
            if (descriptionObject != null && !descriptionObject.isJsonNull) {
                val description = descriptionObject.asString
                row.description = description
            }


            val imageObject = itemJsonObject.get("imageHref")
            if (imageObject != null && !imageObject.isJsonNull) {
                val image = imageObject.asString
                val trimmedUrl = image.substring(5, image.length)
                row.imageHref = "https:$trimmedUrl"
            }
            // add the resource object in the list
            if (!titleObject!!.isJsonNull || !descriptionObject!!.isJsonNull || !imageObject!!.isJsonNull)
                rows!!.add(row)
        }
        return results
    }
}
