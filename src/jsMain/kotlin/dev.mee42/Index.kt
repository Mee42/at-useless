package dev.mee42

import org.w3c.dom.*
import kotlin.browser.document

fun indexEntry(){
    submitButton.onclick = {submitCurrentMessage()}
    dynStorage.submitCurrentMessage = { submitCurrentMessage() }
}

fun submitCurrentMessage(){
    textEntry.value
        .takeUnless { it.isBlank() }
        ?.let { content ->
            addNewMessage(
                author = "Author Placeholder",
                content = content
            )
            textEntry.value = ""
        }
}

fun addNewMessage(author: String, content: String) {
    val newLi = elem<HTMLUListElement>("li") {
        appendChild(elem<HTMLDivElement>("div") {
            classList.add("message")
            appendChild(elem<HTMLParagraphElement>("p") {
                classList.add("message-author")
                innerText = author
            })
            appendChild(elem<HTMLParagraphElement>("p") {
                classList.add("message-content")
                innerText = content
            })
        })
    }
    messageList.appendChild(newLi)
    newLi.scrollIntoView(dyn { behavior = "smooth" })
//    messageList.scrollTo(0.0, messageList.scrollHeight.toDouble())
}

@Suppress("UNCHECKED_CAST")
fun <T : HTMLElement> elem(str: String, conf: T.() -> Unit): T {
    val elem = document.createElement(str) as T
    conf(elem)
    return elem
}


private val messageList by lazyId<HTMLUListElement>("message-list")
private val textEntry by lazyId<HTMLTextAreaElement>("text-input")
private val submitButton by lazyId<HTMLButtonElement>("submit-button")