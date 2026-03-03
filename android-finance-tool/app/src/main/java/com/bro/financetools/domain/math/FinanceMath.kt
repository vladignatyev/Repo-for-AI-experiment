package com.bro.financetools.domain.math

import kotlin.math.pow

object FinanceMath {
  /** Future value with regular monthly contributions (end of period), compounded n times per year. */
  fun futureValueWithContributions(
    principal: Double,
    monthlyContribution: Double,
    annualRate: Double,
    years: Double,
    compoundsPerYear: Int = 12,
  ): Double {
    val n = compoundsPerYear.coerceAtLeast(1)
    val r = annualRate
    val t = years
    val rp = r / n
    val nt = n * t

    val growth = (1 + rp).pow(nt)
    val fvPrincipal = principal * growth
    val fvContrib = if (rp == 0.0) {
      monthlyContribution * 12.0 * years
    } else {
      // Convert monthly contribution to per-compound contribution (approx).
      val pmt = monthlyContribution * (12.0 / n)
      pmt * ((growth - 1) / rp)
    }
    return fvPrincipal + fvContrib
  }

  /** Required portfolio under SWR: annualSpending / swr. */
  fun retirementTarget(annualSpending: Double, swr: Double): Double {
    if (swr <= 0.0) return Double.POSITIVE_INFINITY
    return annualSpending / swr
  }

  /** Monthly payment required to reach a goal FV given initial PV, annual rate, and years. */
  fun monthlyNeededForGoal(
    goalAmount: Double,
    alreadySaved: Double,
    annualRate: Double,
    years: Double,
  ): Double {
    val months = (years * 12.0).coerceAtLeast(1.0)
    val rM = annualRate / 12.0

    val pv = alreadySaved
    if (rM == 0.0) {
      return ((goalAmount - pv) / months).coerceAtLeast(0.0)
    }

    val growth = (1 + rM).pow(months)
    val fvFromPv = pv * growth
    val remaining = (goalAmount - fvFromPv).coerceAtLeast(0.0)
    val annuityFactor = (growth - 1) / rM
    return (remaining / annuityFactor).coerceAtLeast(0.0)
  }
}
