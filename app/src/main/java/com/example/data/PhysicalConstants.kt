package com.example.data

data class PhysicalConstant(
    val symbol: String,
    val name: String,
    val value: Double,
    val formatted: String,
    val unit: String,
    val latex: String
)

/**
 * Authoritative Physical & Mathematical Constants
 * Extracted directly from FocusForge JEE-main (src/services/coach/calculator.ts)
 */
object PhysicalConstants {
    val all = listOf(
        PhysicalConstant("c", "Speed of light in vacuum", 2.99792458e8, "2.998 × 10⁸", "m/s", "c = 3.0 \\times 10^8 \\text{ m/s}"),
        PhysicalConstant("h", "Planck's constant", 6.62607015e-34, "6.626 × 10⁻³⁴", "J·s", "h = 6.626 \\times 10^{-34} \\text{ J}\\cdot\\text{s}"),
        PhysicalConstant("hbar", "Reduced Planck's constant (h/2π)", 1.054571817e-34, "1.055 × 10⁻³⁴", "J·s", "\\hbar = \\frac{h}{2\\pi} = 1.055 \\times 10^{-34} \\text{ J}\\cdot\\text{s}"),
        PhysicalConstant("e", "Elementary charge", 1.602176634e-19, "1.602 × 10⁻¹⁹", "C", "e = 1.602 \\times 10^{-19} \\text{ C}"),
        PhysicalConstant("me", "Electron rest mass", 9.1093837e-31, "9.109 × 10⁻³¹", "kg", "m_e = 9.109 \\times 10^{-31} \\text{ kg}"),
        PhysicalConstant("mp", "Proton rest mass", 1.67262192e-27, "1.673 × 10⁻²⁷", "kg", "m_p = 1.673 \\times 10^{-27} \\text{ kg}"),
        PhysicalConstant("mn", "Neutron rest mass", 1.67492749e-27, "1.675 × 10⁻²⁷", "kg", "m_n = 1.675 \\times 10^{-27} \\text{ kg}"),
        PhysicalConstant("eps0", "Permittivity of free space (ε₀)", 8.8541878128e-12, "8.854 × 10⁻¹²", "F/m", "\\varepsilon_0 = 8.854 \\times 10^{-12} \\text{ F/m}"),
        PhysicalConstant("mu0", "Permeability of free space (μ₀)", 1.25663706212e-6, "4π × 10⁻⁷", "T·m/A", "\\mu_0 = 4\\pi \\times 10^{-7} \\text{ T}\\cdot\\text{m/A}"),
        PhysicalConstant("ke", "Coulomb's constant (1/4πε₀)", 8.9875517923e9, "8.988 × 10⁹", "N·m²/C²", "k_e = \\frac{1}{4\\pi\\varepsilon_0} = 9.0 \\times 10^9 \\text{ N}\\cdot\\text{m}^2/\\text{C}^2"),
        PhysicalConstant("G", "Universal Gravitational constant", 6.6743e-11, "6.674 × 10⁻¹¹", "N·m²/kg²", "G = 6.674 \\times 10^{-11} \\text{ N}\\cdot\\text{m}^2/\\text{kg}^2"),
        PhysicalConstant("R", "Universal gas constant", 8.314462618, "8.314", "J/(mol·K)", "R = 8.314 \\text{ J/(mol}\\cdot\\text{K)}"),
        PhysicalConstant("NA", "Avogadro's number", 6.02214076e23, "6.022 × 10²³", "mol⁻¹", "N_A = 6.022 \\times 10^{23} \\text{ mol}^{-1}"),
        PhysicalConstant("kB", "Boltzmann's constant", 1.380649e-23, "1.381 × 10⁻²³", "J/K", "k_B = 1.381 \\times 10^{-23} \\text{ J/K}"),
        PhysicalConstant("F", "Faraday constant", 96485.33212, "96,485", "C/mol", "F = 96,485 \\text{ C/mol}"),
        PhysicalConstant("g", "Standard gravity acceleration", 9.80665, "9.807", "m/s²", "g = 9.8 \\text{ m/s}^2")
    )
}
