package gentle.hilt.learnunittests2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import gentle.hilt.learnunittests2.adapters.ImageAdapter
import gentle.hilt.learnunittests2.adapters.ShoppingItemAdapter
import gentle.hilt.learnunittests2.repository.FakeDefaultShoppingRepository
import gentle.hilt.learnunittests2.ui.ShoppingViewModel
import gentle.hilt.learnunittests2.ui.fragments.AddShoppingItemFragment
import gentle.hilt.learnunittests2.ui.fragments.PickImageFragment
import gentle.hilt.learnunittests2.ui.fragments.ShoppingFragment
import javax.inject.Inject


class TestShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter
) : FragmentFactory(){
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            PickImageFragment::class.java.name -> PickImageFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter,
            ShoppingViewModel(FakeDefaultShoppingRepository()))
            else -> super.instantiate(classLoader, className)
        }
    }
}