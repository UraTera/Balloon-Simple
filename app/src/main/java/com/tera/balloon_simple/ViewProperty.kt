package com.tera.balloon_simple

import android.view.View
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Расширение для свойств в классах View для инициализации с помощью [ViewPropertyDelegate].
 *
 * @param defaultValue Значение по умолчанию для этого свойства.
 *
 * @return [ViewPropertyDelegate], которое является доступным для чтения и записи свойством.
 */
internal fun <T : Any?> View.viewProperty(defaultValue: T): ViewPropertyDelegate<T> {
  return ViewPropertyDelegate(defaultValue) {
    invalidate()
  }
}

/**
 * Класс делегата для аннулирования класса View, если [propertyValue] было обновлено новым значением.
 *
 * @param defaultValue Значение по умолчанию для этого свойства.
 * @param invalidator Исполняемая лямбда-функция для аннулирования [View].
 *
 * @return Свойство, доступное для чтения и записи.
 */
internal class ViewPropertyDelegate<T : Any?>(
  defaultValue: T,
  private val invalidator: () -> Unit,
) : ReadWriteProperty<Any?, T> {

  private var propertyValue: T = defaultValue

  override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return propertyValue
  }

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    if (propertyValue != value) {
      propertyValue = value
      invalidator()
    }
  }
}
