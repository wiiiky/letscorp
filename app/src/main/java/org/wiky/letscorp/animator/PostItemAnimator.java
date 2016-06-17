package org.wiky.letscorp.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import org.wiky.letscorp.LetscorpApplication;
import org.wiky.letscorp.adapter.PostListAdapter;

import java.util.List;

/**
 * Created by wiky on 6/14/16.
 */
public class PostItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == PostListAdapter.VIEW_TYPE_DEFAULT) {
            runEnterAnimation(viewHolder);
            return false;
        }

        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final RecyclerView.ViewHolder holder) {
        final int width = LetscorpApplication.getScreenWidth();
        holder.itemView.setTranslationX(width);
        holder.itemView.animate()
                .translationX(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(300 + 100 * Math.min(holder.getLayoutPosition(), 6))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        dispatchAddFinished(holder);
                        holder.itemView.setTranslationX(0);
                    }
                })
                .start();
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);

        return false;
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
    }

}
