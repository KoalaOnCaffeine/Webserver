package me.tomnewton.shared

interface DataObject {

    /**
     * Creates a json representation of the [DataObject], with all of its fields
     * @return a json string of the [DataObject]
     */

    fun toJsonObject(): String

    /**
     * Creates a json representation of the [DataObject], which does not contain sensitive information
     * @return a json object containing only public information
     */

    fun toSensitiveJsonObject(): String
}