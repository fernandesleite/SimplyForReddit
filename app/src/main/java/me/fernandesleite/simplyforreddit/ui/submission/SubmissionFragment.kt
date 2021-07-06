package me.fernandesleite.simplyforreddit.ui.submission

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.home.SharedSubmissionViewModelBase

class SubmissionFragment : Fragment() {

    private val sharedSubmissionViewModel: SharedSubmissionViewModelBase by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submission_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val author: TextView = view.findViewById(R.id.author)
        val title: TextView = view.findViewById(R.id.title)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val score: TextView = view.findViewById(R.id.score)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val subreddit: TextView = view.findViewById(R.id.subreddit)

        sharedSubmissionViewModel.submission.observe(viewLifecycleOwner, { submissionInfo ->
            title.text = submissionInfo.title
            author.text = "u/${submissionInfo.author}"
            commentCount.text = "${submissionInfo.commentCount.toString()} comments"
            score.text = submissionInfo.score.toString()
            subreddit.text = "r/${submissionInfo.subreddit}"
            thumbnail.setImageDrawable(null)
            if (URLUtil.isValidUrl(submissionInfo.thumbnail)) {
                Glide.with(thumbnail.context)
                    .load(submissionInfo.thumbnail)
                    .centerCrop()
                    .into(thumbnail)
                thumbnail.layoutParams.width = 263
            } else {
                Glide.with(thumbnail.context).clear(thumbnail)
                thumbnail.layoutParams.width = 0
            }
            thumbnail.setOnClickListener {
                when (submissionInfo.postHint) {
                    "link" -> findNavController().navigate(
                        SubmissionFragmentDirections.actionSubmissionFragmentToBrowserFragment(
                            submissionInfo.url
                        )
                    )
                    "image" -> findNavController().navigate(
                        SubmissionFragmentDirections.actionSubmissionFragmentToImageViewFragment(
                            submissionInfo.url
                        )
                    )
                    "hosted:video" -> {
                        findNavController().navigate(
                            SubmissionFragmentDirections.actionSubmissionFragmentToVideoPlayerFragment(
                                submissionInfo.embeddedMedia?.redditVideo?.dashUrl
                            )
                        )
                    }
                    "rich:video" -> {
                        if (submissionInfo.domain == "youtube.com") {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(submissionInfo.url))
                            this.startActivity(intent)
                        } else {
                            findNavController().navigate(
                                SubmissionFragmentDirections.actionSubmissionFragmentToBrowserFragment(
                                    submissionInfo.url
                                )
                            )
                        }
                    }
                }
            }
        })
    }
}