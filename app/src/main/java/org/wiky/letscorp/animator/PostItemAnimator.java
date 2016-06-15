package org.wiky.letscorp.animator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by wiky on 6/14/16.
 */
public class PostItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        // We reuse ViewHolders to allow for efficient change notifications via payload.
        return true;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                     @Nullable ItemHolderInfo preLayoutInfo,
                                     @NonNull ItemHolderInfo postLayoutInfo) {
        final View view = viewHolder.itemView;

        if (preLayoutInfo != null &&
                (preLayoutInfo.changeFlags & FLAG_APPEARED_IN_PRE_LAYOUT) != 0) {
            // Items added in prelayout by the LayoutManager don't have a decor offset while they
            // animate.
            translateByDecorOffset(view, preLayoutInfo);
        }

        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                        @NonNull ItemHolderInfo preLayoutInfo,
                                        @Nullable ItemHolderInfo postLayoutInfo) {
        final View view = viewHolder.itemView;

        if ((preLayoutInfo.changeFlags & FLAG_CHANGED) != 0) {
            // Undo the bad translation applied in animateChange.
            ViewCompat.setTranslationY(view, 0);
        }

        if ((preLayoutInfo.changeFlags & FLAG_REMOVED) != 0) {
            // Disappearing items added in prelayout by the LayoutManager don't have a decor
            // offset while they animate.
            translateByDecorOffset(view, preLayoutInfo);
        }
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public void onAddFinished(RecyclerView.ViewHolder item) {
        // Clear the translation we applied for the appearance animation.
        ViewCompat.setTranslationY(item.itemView, 0);
    }

    @Override
    public void onRemoveFinished(RecyclerView.ViewHolder item) {
        // Clear the translation we applied for the disappearance animation.
        ViewCompat.setTranslationY(item.itemView, 0);
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(
            @NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder,
            int changeFlags, @NonNull List<Object> payloads) {
        ItemHolderInfo info = obtainHolderInfo().setFrom(viewHolder);
        // The default implementation drops changeFlags for some reason.
        info.changeFlags = changeFlags;
        return info;
    }

    private void translateByDecorOffset(View view, ItemHolderInfo preLayoutInfo) {
        // Calculate the offset added by decor before it disappeared between pre- and post-layout.
        // Translate the view by this offset to avoid bad positioning during layout animations.
        RecyclerView.LayoutManager lm = ((RecyclerView) view.getParent()).getLayoutManager();
        int oldTop = lm.getDecoratedTop(view);
        int newTop = preLayoutInfo.top;
        int deltaY = newTop - oldTop;
        if (deltaY != 0) {
            view.setTranslationY(deltaY);
        }
    }
}
