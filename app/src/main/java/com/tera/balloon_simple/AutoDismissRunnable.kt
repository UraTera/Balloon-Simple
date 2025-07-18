package com.tera.balloon_simple

/**
 * Класс, реализующий [Runnable] для закрытия [Balloon]
 * если [Balloon.Builder.autoDismissDuration] имеет значение.
 */
internal class AutoDismissRunnable(val balloon: Balloon) : Runnable {

  /** убрать Balloon. */
  override fun run() {
    balloon.dismiss()
  }
}
