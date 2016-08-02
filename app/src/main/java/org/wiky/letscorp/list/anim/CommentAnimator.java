package org.wiky.letscorp.list.anim;

import android.animation.Animator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import org.wiky.letscorp.Application;

/* 评论列表的动画 */
public class CommentAnimator extends DefaultItemAnimator {


    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        runEnterAnimation(viewHolder);
        return false;
    }

    private void runEnterAnimation(final RecyclerView.ViewHolder holder) {
        int width = Application.getScreenWidth();
        holder.itemView.setAlpha(0.0f);
        holder.itemView.setTranslationX(width);
        holder.itemView.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(250)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(30 * holder.getLayoutPosition())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    private void end() {
                        holder.itemView.setAlpha(1.0f);
                        holder.itemView.setTranslationX(0.0f);
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        end();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        end();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}
