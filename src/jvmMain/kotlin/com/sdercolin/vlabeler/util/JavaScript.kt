package com.sdercolin.vlabeler.util

import kotlinx.serialization.Serializable
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Source
import org.graalvm.polyglot.Value
import java.io.Closeable
import java.io.File
import java.util.logging.Handler

/**
 * A wrapper of JavaScript engine powered by GraalVM.
 */
class JavaScript(logHandler: Handler? = null, currentWorkingDirectory: File? = null) : Closeable {

    private val context = Context.newBuilder()
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup { true }
        .runIfHave(logHandler) { logHandler(it) }
        .allowIO(true)
        .currentWorkingDirectory(currentWorkingDirectory?.toPath() ?: HomeDir.toPath())
        .option("engine.WarnInterpreterOnly", "false")
        .build()

    private val bindings get() = context.getBindings("js")

    fun eval(source: String): Value? = context.eval("js", source)

    fun exec(sourceFileName: String, source: String) {
        context.eval(Source.newBuilder("js", source, sourceFileName).build())
    }

    /**
     * Only for primitives. For other types, use [getJson]
     */
    fun <T : Any> getOrNull(name: String, ofClass: Class<T>): T? {
        val value = bindings.getMember(name) ?: return null
        if (value.isNull) return null
        return value.`as`(ofClass)
    }

    /**
     * Only for primitives. For other types, use [getJson]
     */
    inline fun <reified T : Any> get(name: String): T {
        return requireNotNull(getOrNull(name, T::class.java))
    }

    /**
     * Only for primitives. For other types, use [getJson]
     */
    inline fun <reified T : Any> getOrNull(name: String): T? {
        return getOrNull(name, T::class.java)
    }

    /**
     * Only for primitives. For other types, use [setJson]
     */
    fun set(name: String, value: Any?) {
        bindings.putMember(name, value)
    }

    /**
     * Pass object to JavaScript via JSON serialization
     */
    inline fun <reified T : @Serializable Any> setJson(name: String, value: T) {
        set(name, value.stringifyJson())
        eval("$name = JSON.parse($name)")
    }

    /**
     * Receive object from JavaScript via JSON deserialization
     */
    inline fun <reified T : @Serializable Any> getJson(name: String): T {
        val json = eval("JSON.stringify($name)")!!.asString()
        return json.parseJson()
    }

    /**
     * Receive object from JavaScript via JSON deserialization
     */
    inline fun <reified T : @Serializable Any> getJsonOrNull(name: String): T? {
        val undefined = eval("typeof($name) == \"undefined\" || $name == null")?.asBoolean() != false
        if (undefined) return null
        return getJson(name)
    }

    /**
     * Only for JavaScript array types with unserializable elements. For serializable types, use [getJson]
     */
    fun <T : Any> getArrayOrNull(name: String, ofClass: Class<T>): List<T>? {
        val value = bindings.getMember(name) ?: return null
        if (value.isNull) return null
        if (!value.hasArrayElements()) return null
        return List(value.arraySize.toInt()) {
            value.getArrayElement(it.toLong()).`as`(ofClass)
        }
    }

    /**
     * Only for JavaScript array types with unserializable elements. For serializable types, use [getJson]
     */
    inline fun <reified T : Any> getArray(name: String): List<T> {
        return requireNotNull(getArrayOrNull(name, T::class.java))
    }

    /**
     * Only for JavaScript array types with unserializable elements. For serializable types, use [getJson]
     */
    inline fun <reified T : Any> getArrayOrNull(name: String): List<T>? {
        return getArrayOrNull(name, T::class.java)
    }

    /**
     * Only for JavaScript array types with unserializable elements. For serializable types, use [setJson]
     */
    fun setArray(name: String, list: List<Any?>) {
        val array = eval("[]")!!
        list.forEachIndexed { index, value -> array.setArrayElement(index.toLong(), value) }
        bindings.putMember(name, array)
    }

    override fun close() {
        context.close()
    }
}
