package org.ergoplatform.mosaik

import java.lang.IllegalArgumentException

class ElementNotFoundException(msg: String, val elementId: String) : IllegalArgumentException(msg)

class InvalidValuesException(msg: String, val errorList: String) : IllegalArgumentException(msg)