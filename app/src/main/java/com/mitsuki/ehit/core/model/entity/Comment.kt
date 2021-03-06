package com.mitsuki.ehit.core.model.entity

import androidx.recyclerview.widget.DiffUtil
import com.mitsuki.ehit.core.model.ehparser.byClassFirst
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.NodeTraversor
import org.jsoup.select.NodeVisitor

class Comment(val id: Int, val time: String, val user: String, val text: String) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldConcert: Comment,
                newConcert: Comment
            ): Boolean =
                oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: Comment,
                newConcert: Comment
            ): Boolean {
                return oldConcert.time == newConcert.time &&
                        oldConcert.user == newConcert.user &&
                        oldConcert.text == newConcert.text
            }
        }


        fun parse(element: Element): Comment {
            val id =
                element.previousElementSibling().attr("name").substring(1).toInt()

            //val c4Node = element.byClassFirst("c4") 这部分暂不解析

            val c3Node = element.byClassFirst("c3", "c3 node".prefix())

            val time =
                with(c3Node.ownText()) { substring("Posted on ".length, length - " by:".length) }

            val user = c3Node.child(0).text()

            val text = element.byClassFirst("c6", "comment text (c6 node text)".prefix()).html()

            return Comment(id, time, user, text)
        }

        private fun String.prefix(): String = "Parse comment: not found $this"
    }
}

@Suppress("ArrayInDataClass")
data class CommentSet(val comments: Array<Comment>, var hasMore: Boolean) {
    companion object {
        fun parse(element: Element): CommentSet {

            val comments = with(element.getElementsByClass("c1")) {
                Array(size) { number -> Comment.parse(this[number]) }
            }

            var temp = false
            NodeTraversor.traverse(object : NodeVisitor {
                override fun head(node: Node?, depth: Int) {
                    if (node is Element && node.text() == "click to show all") temp = true
                }

                override fun tail(node: Node?, depth: Int) {}
            }, element.getElementById("chd"))
            return CommentSet(comments, temp)
        }
    }
}