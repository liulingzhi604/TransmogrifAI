package com.salesforce.op.stages.impl.feature

import com.salesforce.op.UID
import com.salesforce.op.features.types.Text
import com.salesforce.op.stages.base.unary.UnaryTransformer

class IdRemover(
  minUniqueTokLen: Int,
  uid: String = UID[IdRemover],
  operationName: String = "IDremover"
) extends UnaryTransformer[Text, Text] (operationName = operationName, uid = uid) {

  private var drop: Boolean = false

  override protected def onSetInput(): Unit = {
    super.onSetInput()
    val dist = in1.asFeatureLike.distributions
    val tokenLenCardFilter = dist.flatMap(_.cardEstimate).map(_.valueCounts.size < minUniqueTokLen)
    drop = tokenLenCardFilter.headOption.getOrElse(false)
  }

  override def transformFn: Text => Text = a => if (drop) Text.empty else a
}
