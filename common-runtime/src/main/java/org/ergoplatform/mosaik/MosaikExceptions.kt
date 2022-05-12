package org.ergoplatform.mosaik

import java.lang.IllegalArgumentException

class ElementNotFoundException(msg: String, val elementId: String) : IllegalArgumentException(msg)