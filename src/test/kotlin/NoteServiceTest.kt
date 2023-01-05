import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun addNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        val result = NoteService.items.size
        assertEquals(2, result)
    }

    @Test
    fun getNoteById() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        val note = NoteService.getById(4)
        val result = note.nid
        assertEquals(4,result)
    }

    @Test(expected = ItemNotFoundException::class)
    fun getNoteByIdShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.getById(10)

    }

    @Test
    fun deleteNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        val result = NoteService.delete(3)
        assertTrue(result)
    }

    @Test(expected = ItemNotFoundException::class)
    fun deleteNoteShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.delete(6)
    }

    @Test
    fun editNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.edit(nid = 3, newTitle = "Title for new note", newText = "Hi. It's test")
        val result = NoteService.items[2]
        val note = Note(nid = 3, title = "Title for new note", text = "Hi. It's test")
        assertEquals(note, result)
    }

    @Test
    fun createCommentToNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        val result = NoteService.items[0].comments.size
        assertEquals(3, result)
    }

    @Test(expected = ItemNotFoundException::class)
    fun createCommentToUnexistingNoteShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 10)
    }

    @Test
    fun editCommentToNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.editComment(cid = 2, newMessage = "It's test comment")
        val comment = CommentToNote(cid = 2, message = "It's test comment")
        assertEquals(comment, NoteService.items[0].comments[1])
    }

    @Test(expected = ItemNotFoundException::class)
    fun editCommentToUnexistingNoteShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.editComment(cid = 20, newMessage = "It's test comment")
    }
    @Test(expected = ItemNotFoundException::class)
    fun editDeletedCommentToNoteShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.deleteComment(3)
        NoteService.editComment(cid = 3, newMessage = "It's test comment")
    }
    @Test
    fun getCommentsToNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.editComment(cid = 2, newMessage = "It's test comment")
        NoteService.createComment(nid = 1)
        assertEquals(4, NoteService.items[0].comments.size)
    }

    @Test(expected = ItemNotFoundException::class)
    fun getCommentsToUnexistingNoteShouldThrow() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.editComment(cid = 2, newMessage = "It's test comment")
        NoteService.createComment(nid = 1)
        NoteService.getComments(20)
    }

    @Test
    fun deleteCommentToNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)

        assertTrue(NoteService.deleteComment(2))
    }
    @Test
    fun restoreCommentToNote() {
        NoteService.add(Note(NoteService.counter))
        NoteService.add(Note(NoteService.counter))
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.createComment(nid = 1)
        NoteService.deleteComment(2)
        NoteService.restoreComment(2)
        assertTrue(NoteService.restoreComment(2))
    }

}