package ru.otus.otuskotlin.catalogue.backend.common.models

/**
 * Класс подразделов с добавлением уникальных подразделов
 * убрал пока
 */
class ChildrenModel<T>: ArrayList<T>() {

    override fun add(element: T): Boolean {
        this.forEach {
            if(it == element) return false
        }
        return super.add(element)
    }

    override fun add(index: Int, element: T) {
        this.forEach {
            if(it == element) return
        }
        super.add(index, element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val list: MutableList<T> = mutableListOf()
        loop@ for (element in elements){
            for (category in this){
                if (element == category) continue@loop
            }
            list.add(element)
        }
        return super.addAll(list)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val list: MutableList<T> = mutableListOf()
        loop@ for (element in elements){
            for (category in this){
                if (element == category) continue@loop
            }
            list.add(element)
        }
        return super.addAll(index, list)
    }
}