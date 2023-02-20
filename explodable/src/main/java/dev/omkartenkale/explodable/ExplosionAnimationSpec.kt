package dev.omkartenkale.explodable

data class ExplosionAnimationSpec(
    val shakeDurationMs: Int = 250,
    val explosionPower: Float = 2f,
    val explosionDurationMs: Int = 750
) {

    init {
        if (explosionPower <= 0) {
            throw IllegalArgumentException("explosionPower must be > 0")
        }
        if (shakeDurationMs < 0) {
            throw IllegalArgumentException("shakeDurationMs must be >= 0")
        }
        if (explosionDurationMs < 0) {
            throw IllegalArgumentException("explosionDurationMs must be >= 0")
        }
    }

    companion object {
        const val SHRINK_DURATION_MS = 75
    }
}
