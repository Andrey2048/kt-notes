open class Item(var id: Int)

class ItemNotFoundException(message: String) : RuntimeException(message)

open class CrudService<T : Item> {
    protected val items = mutableListOf<T>()
    var counter = 1
    fun add(item: T): T {
        item.id = counter++
        items.add(item)
        return items.last()
    }

    fun getById(id: Int): T {
        for ((index, elem) in items.withIndex()) {
            if (elem.id == id) return items[index]
        }
        throw ItemNotFoundException("No item with id $id")
    }

    fun get(): MutableList<T> {
        return items
    }

    fun update(item: T): Boolean {
        for ((index, elem) in items.withIndex()) {
            if (elem.id == item.id) {
                items[index] = item
                return true
            }
        }
        return false
    }

    fun delete(id: Int): Boolean {
        for (elem in items) {
            if (elem.id == id) {
                items.remove(elem)
                return true
            }
        }
        throw ItemNotFoundException("No item with id $id")

    }

    fun printAllItems() {
        for (item in items) println(item)
    }

    fun isItemWithId(id: Int): Boolean {
        var result = false
        for (elem in items) if (elem.id == id) result = true
        return result
    }


}

data class Note(
    var nid: Int,
    var title: String = "", //Заголовок заметки
    val text: String = "", //Текст заметки
    val comments: MutableList<CommentToNote> = mutableListOf()
) : Item(nid)

data class CommentToNote(
    val cid: Int = 0, //Идентификатор комментария
    val message: String = "", //Текст комментария
    var isDeleted: Boolean = false // true - был удален и не отображается, false - ранее не удалялся
)

object NoteService : CrudService<Note>() {
    private var counterCommentId = 1

    fun edit(nid: Int, newTitle: String, newText: String): Boolean {
        val newItem = Note(nid, newTitle, newText)
        for (item in items) {
            if (item.nid == nid) newItem.nid = nid
        }
        return update(newItem)
    }

    fun createComment(nid: Int): CommentToNote {
        val comment = CommentToNote(cid = counterCommentId++)
        if (isItemWithId(nid)) {
            for (item in items) {
                if (item.id == nid) {
                    item.comments.add(comment)
                }
            }
        } else throw ItemNotFoundException("No note with id $nid")
        return comment
    }

    fun editComment(cid: Int, newMessage: String): Boolean {
        val editedComment = CommentToNote(cid, newMessage)
        for (item in items) {
            for ((index, comment) in item.comments.withIndex()) {
                if (comment.cid == cid) {
                    if (!comment.isDeleted) {
                        item.comments[index] = editedComment
                        return true
                    } else throw ItemNotFoundException("No comment with id $cid")
                }
            }
        }
        return false
    }

    fun getComments(nid: Int): MutableList<CommentToNote> {
        var result = mutableListOf<CommentToNote>()
        for (item in items) {
            if (item.nid == nid) {
                result = item.comments
            }
        }
        return result
    }

    fun deleteComment(cid: Int): Boolean {
        for (item in items) {
            for (comment in item.comments) {
                if (comment.cid == cid) {
                    if (!comment.isDeleted) {
                        comment.isDeleted = true
                        return true
                    } else throw ItemNotFoundException("No comment with id $cid")
                }
            }
        }
        return false
    }

    fun restoreComment(cid: Int): Boolean {
        for (item in items) {
            for (comment in item.comments) {
                if (comment.cid == cid) {
                    comment.isDeleted = false
                    return true
                }
            }
        }
        return false
    }

    fun printAllComments(noteId: Int, printDeleted: Boolean = true) {
        for (item in items) {
            if (item.id == noteId) println(if (printDeleted) item.comments else item.comments.filter { !it.isDeleted })
        }
    }

    fun printCommentNote(cid: Int) {
        val nid = findNoteByCommentId(cid)
        printAllComments(nid)
    }

    fun findNoteByCommentId(cid: Int): Int {
        var nid = -1
        for (item in items) {
            for (comment in item.comments) {
                if (comment.cid == cid) nid = item.id
            }
        }
        return nid
    }
}


fun main() {
    NoteService.add(Note(NoteService.counter))
    NoteService.add(Note(NoteService.counter))
    NoteService.add(Note(NoteService.counter))
    NoteService.add(Note(NoteService.counter))
    NoteService.printAllItems()
    println()
    NoteService.delete(1)
    NoteService.printAllItems()
    println()

    NoteService.edit(nid = 2, newTitle = "Заголовочек", newText = "текст заметки 2")
    println(NoteService.get())
    println(NoteService.getById(2))
    println()
    NoteService.createComment(2)
    NoteService.createComment(2)
    NoteService.createComment(3)
    NoteService.createComment(3)
    NoteService.printAllComments(2)
    NoteService.printAllComments(3)
    println()
    NoteService.deleteComment(3)

    NoteService.printAllComments(3, true)
    NoteService.printAllComments(3, false)
    println(NoteService.findNoteByCommentId(1))
    println(NoteService.findNoteByCommentId(2))
    println(NoteService.findNoteByCommentId(3))

    NoteService.restoreComment(3)
    NoteService.editComment(3, "New comment")
    NoteService.printCommentNote(3)
    println()
    println(NoteService.getComments(4))
    println(NoteService.getComments(3))
    println(NoteService.getComments(2))
    println(NoteService.getComments(1))
}


