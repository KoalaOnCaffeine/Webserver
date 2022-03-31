package me.tomnewton.shared

interface DataObject {

    /**
     * Creates a json representation of the [DataObject], with all of its fields
     * @return a json string of the [DataObject]
     */

    fun toJsonObject(): String

}