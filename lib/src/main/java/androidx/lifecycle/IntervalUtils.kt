package androidx.lifecycle

import com.chenyue404.androidlib.util.Interval

/** 轮询器根据ViewModel生命周期自动取消 */
fun Interval.life(viewModel: ViewModel) = apply {
    viewModel.setTagIfAbsent(toString(), this)
}