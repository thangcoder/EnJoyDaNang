package node.com.enjoydanang.ui.fragment.review;

import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;

/**
 * Author: Tavv
 * Created on 07/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class DummyDataReview {

    public static List<Review> dummyData() {
        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        review.setName("Tá Võ");
        review.setStar(4);
        review.setId(12);
        review.setAvatar("https://s-media-cache-ak0.pinimg.com/originals/b0/d5/48/b0d5488ae58944e19156f6591f5b09ce.jpg");
        review.setTitle("Review");
        review.setContent("Just content of dummy review");
        review.setInterpolator(Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR));
        review.setDate("2017-11-06T18:38:56.09");

        List<String> images = new ArrayList<>();
        images.add("https://i.pinimg.com/736x/60/6e/bb/606ebbc439f8191df82c058ba534010a--bob-ross-paintings-oil-paintings.jpg");
        images.add("https://i.pinimg.com/736x/2a/63/9a/2a639a2d431aea53b54da079f7dda40e--canvas-painting-ideas-for-beginners-acrylics-painting-inspiration-ideas.jpg");
        images.add("http://artunframed.com/Gallery/wp-content/uploads/2013/07/The-Tree-of-Crows-Caspar-David-Friedrich.jpg");

        review.setImages(images);

        List<Reply> replies = new ArrayList<>();
        Reply reply = new Reply();
        reply.setImages(images);
        reply.setAvatar("https://img.saostar.vn/w500/2017/04/17/1227207/justin.jpg");
        reply.setUserName("Iris Louis");
        reply.setId(10);
        reply.setReviewId(12);
        reply.setContent("Just content of dummy reply");

        Reply reply1 = new Reply();
        reply1.setImages(images);
        reply1.setAvatar("https://img.saostar.vn/w500/2017/04/17/1227207/justin.jpg");
        reply1.setUserName("Iris Louis");
        reply1.setId(10);
        reply1.setReviewId(12);
        reply1.setContent("Just content of dummy reply");


        Reply reply2 = new Reply();
        reply2.setImages(images);
        reply2.setAvatar("https://img.saostar.vn/w500/2017/04/17/1227207/justin.jpg");
        reply2.setUserName("Iris Louis");
        reply2.setId(10);
        reply2.setReviewId(12);
        reply2.setContent("Just content of dummy reply");


        Reply reply3 = new Reply();
        reply3.setImages(images);
        reply3.setAvatar("https://img.saostar.vn/w500/2017/04/17/1227207/justin.jpg");
        reply3.setUserName("Iris Louis");
        reply3.setId(10);
        reply3.setReviewId(12);
        reply3.setContent("Just content of dummy reply");

        replies.add(reply);

        replies.add(reply1);

        replies.add(reply2);

        replies.add(reply3);

        review.setReplies(replies);

        reviews.add(review);
        return reviews;
    }
}
