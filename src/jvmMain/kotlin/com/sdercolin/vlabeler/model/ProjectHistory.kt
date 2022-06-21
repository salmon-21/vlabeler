package com.sdercolin.vlabeler.model

data class ProjectHistory(
    val list: List<Project> = listOf(),
    val index: Int = -1
) {
    val current get() = list[index]
    val canUndo get() = index > 0
    val canRedo get() = index >= 0 && index < list.lastIndex

    fun new(project: Project) = ProjectHistory(list = listOf(project), 0)

    fun push(project: Project) = (list.subList(0, index + 1) + project)
        .squashLatest()
        .takeLast(MaxStackSize)
        .let {
            ProjectHistory(
                list = it,
                index = it.lastIndex
            )
        }

    private fun List<Project>.squashLatest(): List<Project> {
        if (size < 2) return this
        val (prev, last) = takeLast(2)
        return if (
            prev.copy(
                currentSampleName = last.currentSampleName,
                currentEntryIndex = last.currentEntryIndex
            ) == last
        ) {
            // only index changed, remove the previous one
            dropLast(2) + last
        } else this
    }

    fun undo() = copy(index = index.minus(1).coerceAtLeast(0))

    fun redo() = copy(index = index.plus(1).coerceAtMost(list.lastIndex))

    companion object {
        private const val MaxStackSize = 100
    }
}