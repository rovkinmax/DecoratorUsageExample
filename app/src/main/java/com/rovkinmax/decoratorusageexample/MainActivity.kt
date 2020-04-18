package com.rovkinmax.decoratorusageexample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osome.stickydecorator.ConditionItemDecorator
import com.rovkinmax.decoratorusageexample.recycler.decoration.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MessagesAdapter() { recyclerView.scrollToPosition(0) }
    private val noDecorationAdapter = NoDecorationMessageAdapter() { recyclerView.scrollToPosition(0) }
    private val messagesList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSendMy.setOnClickListener { addMyMessage() }
        btnSendFromBack.setOnClickListener { addAgentMessage() }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        // because current implementation for item decorations don't support animation
        // potential solution https://www.reddit.com/r/androiddev/comments/2vv27p/recyclerview_itemdecorations_arent_animated_with/
        recyclerView.itemAnimator = null

        initialState()
    }

    private fun initialState() {
        messagesList.clear()
        messagesList.addAll(MessageFactory.defaultMessageSet())
        setUpDecorationExample()
    }

    private fun setUpDecorationExample() {
        recyclerView.adapter = null
        cleanDecoration()
        recyclerView.addItemDecoration(buildSpaceDecoration())
        recyclerView.addItemDecoration(buildTimeStampDecoration())
        recyclerView.addItemDecoration(buildAvatarDecoration())
        recyclerView.addItemDecoration(buildBubbleDecoration())
        recyclerView.addItemDecoration(buildSectionDecoration())
        adapter.setList(messagesList)
        recyclerView.adapter = adapter
    }

    private fun setUpNoDecorationExample() {
        recyclerView.adapter = null
        cleanDecoration()
        recyclerView.addItemDecoration(buildSpaceDecoration())
        noDecorationAdapter.setList(messagesList)
        recyclerView.adapter = noDecorationAdapter
    }

    private fun cleanDecoration() {
        (0 until recyclerView.itemDecorationCount)
                .map { index -> recyclerView.getItemDecorationAt(index) }
                .forEach { decoration -> recyclerView.removeItemDecoration(decoration) }
    }

    private fun buildSpaceDecoration(): RecyclerView.ItemDecoration {
        val horizontalSpace = 12.px2dp
        val verticalSpace = 4.px2dp
        return SpaceDecoration(
                left = horizontalSpace,
                right = horizontalSpace,
                vertical = verticalSpace
        )
    }

    private fun buildTimeStampDecoration(): RecyclerView.ItemDecoration {
        return ConditionItemDecorator(
                ChatTimeStampCondition(adapter),
                TimeStampDecor(adapter, resources)
        )
    }

    private fun buildAvatarDecoration(): RecyclerView.ItemDecoration {
        return AvatarDecorator(callback = ChatAvatarHelper(adapter))
    }

    private fun buildBubbleDecoration(): RecyclerView.ItemDecoration {
        return BubbleDecorator(resources, adapter, ChatTimeStampCondition(adapter))
    }

    private fun buildSectionDecoration(): RecyclerView.ItemDecoration {
        val helper = ChatDateSeparatorHelper(adapter)
        return ConditionItemDecorator(helper, DateSectionDecor(resources, helper))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.withDecoration -> setUpDecorationExample()
            R.id.noDecoration -> setUpNoDecorationExample()
            R.id.reset -> initialState()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMyMessage() {
        messagesList.add(0, MessageFactory.buildMyMessage())
        updateAdapters()
    }

    private fun addAgentMessage() {
        messagesList.add(0, MessageFactory.buildAgentMessage())
        updateAdapters()
    }

    private fun updateAdapters() {
        noDecorationAdapter.setList(messagesList)
        adapter.setList(messagesList)
    }
}