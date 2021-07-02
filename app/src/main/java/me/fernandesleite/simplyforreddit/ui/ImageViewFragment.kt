package me.fernandesleite.simplyforreddit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import me.fernandesleite.simplyforreddit.R
import pl.droidsonroids.gif.GifDecoder
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView


class ImageViewFragment : Fragment() {

    private val args: ImageViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        val mediaController: MediaController = MediaController(requireContext())
//        mediaController.setMediaPlayer(image.drawable as GifDrawable)
//        mediaController.setAnchorView(image)
//        mediaController.show()
    }
}