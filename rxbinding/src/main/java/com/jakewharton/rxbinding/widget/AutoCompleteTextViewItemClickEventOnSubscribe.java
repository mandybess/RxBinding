package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AutoCompleteTextViewItemClickEventOnSubscribe
    implements Observable.OnSubscribe<AdapterViewItemClickEvent> {
  final AutoCompleteTextView view;

  public AutoCompleteTextViewItemClickEventOnSubscribe(AutoCompleteTextView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super AdapterViewItemClickEvent> subscriber) {
    checkUiThread();

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(AdapterViewItemClickEvent.create(parent, view, position, id));
        }
      }
    };
    view.setOnItemClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnItemClickListener(null);
      }
    });
  }
}
