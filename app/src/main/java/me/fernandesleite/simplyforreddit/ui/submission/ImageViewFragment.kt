package me.fernandesleite.simplyforreddit.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import me.fernandesleite.simplyforreddit.R
import pl.droidsonroids.gif.GifImageView


class ImageViewFragment : Fragment() {

    private val args: ImageViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(args.url).into(view.findViewById(R.id.imageView))
        val image = view.findViewById<GifImageView>(R.id.imageView)
        val mediaController  = MediaController(requireContext())
//        mediaController.setMediaPlayer(image.drawable as GifDrawable)
//        mediaController.setAnchorView(image)
//        mediaController.show()
    }
}