package org.wiky.letscorp.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
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
        int height = LetscorpApplication.getScreenHeight();
        holder.itemView.setTranslationY(height / 2.0f);
        holder.itemView.setAlpha(0.0f);
        holder.itemView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .setStartDelay(100 * Math.min(holder.getLayoutPosition(), 5))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        holder.itemView.setTranslationY(0);
                        holder.itemView.setAlpha(1.0f);
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == PostListAdapter.VIEW_TYPE_DEFAULT) {
            runExitAnimation(viewHolder);
        } else {
            runLoaderExitAnimation(viewHolder);
        }
        return false;
    }

    private void runExitAnimation(final RecyclerView.ViewHolder holder) {
        int height = LetscorpApplication.getScreenHeight();
        holder.itemView.setClickable(false);
        holder.itemView.animate()
                .translationY(-height / 2.0f)
                .alpha(0.0f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    private void end() {
                        holder.itemView.setTranslationY(0.0f);
                        holder.itemView.setAlpha(1.0f);
                        holder.itemView.setClickable(true);
                        dispatchAddFinished(holder);
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        end();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        end();
                    }
                })
                .start();
    }

    private void runLoaderExitAnimation(final RecyclerView.ViewHolder holder) {
        holder.itemView.animate()
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        holder.itemView.setAlpha(1.0f);
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.itemView.setAlpha(1.0f);
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }


    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
    }

}
