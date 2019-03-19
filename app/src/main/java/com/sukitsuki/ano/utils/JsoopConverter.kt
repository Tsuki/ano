package com.sukitsuki.ano.utils

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector

class ExistConverter : ElementConverter<Boolean> {
  override fun convert(node: Element, selector: Selector): Boolean {
    return true
  }

}
