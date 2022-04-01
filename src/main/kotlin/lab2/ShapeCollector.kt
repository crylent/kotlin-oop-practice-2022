package lab2

import kotlin.reflect.KClass

class ShapeCollector(initialShapes: List<ColoredShape2d> = emptyList()) {
    private val shapesList: MutableList<ColoredShape2d> = initialShapes.toMutableList()
    val shapes: List<ColoredShape2d>
        get() = shapesList.toList()

    constructor(shape: ColoredShape2d) : this(listOf(shape)) // construct from only one shape

    fun add(shape: ColoredShape2d) { // add one shape
        shapesList.add(shape)
    }

    fun add(shapesToAdd: List<ColoredShape2d>) { // add all shapes from list
        shapesList.addAll(shapesToAdd)
    }

    fun add(shapesToAdd: ShapeCollector) { // add all shapes from another collector
        shapesList.addAll(shapesToAdd.shapesList)
    }

    val size: Int
        get() = shapesList.size

    val totalArea: Double
        get() {
            var area = 0.0
            for (shape in shapesList) {
                area += shape.calcArea()
            }
            return area
        }

    fun find(shapeClass: KClass<out ColoredShape2d>): MutableList<ColoredShape2d> { // find shapes by class
        val list = emptyList<ColoredShape2d>().toMutableList()
        for (shape in shapesList) {
            if (shape::class == shapeClass)
                list.add(shape)
        }
        return list
    }

    val smallestArea = false
    val biggestArea = true
    fun findByArea(mode: Boolean): ColoredShape2d? { // findByArea(false) -> find the smallest, findByArea(true) -> find the biggest
        var best: ColoredShape2d? = null
        for (shape in shapesList) {
            if (mode == smallestArea && shape.calcArea() < (best?.calcArea() ?: Double.MAX_VALUE)
                || mode == biggestArea && shape.calcArea() > (best?.calcArea() ?: 0.0)
            ) best = shape
        }
        return best
    }

    val borderColor = false
    val fillColor = true
    fun findByColor(mode: Boolean, color: Color): List<ColoredShape2d> { // findByColor(false) -> find by border color, findByColor(true) -> find by fill color
        val list = emptyList<ColoredShape2d>().toMutableList()
        for (shape in shapesList) {
            if (mode == borderColor && shape.borderColor == color || mode == fillColor && shape.fillColor == color)
                list.add(shape)
        }
        return list
    }

    fun groupByColor(mode: Boolean): Map<Color, List<ColoredShape2d>> { // groupByColor(false) -> group by border color, findByColor(true) -> group by fill color
        val map = emptyMap<Color, MutableList<ColoredShape2d>>().toMutableMap()
        for (shape in shapesList) {
            val color = if (mode == borderColor) shape.borderColor else shape.fillColor
            if (map.containsKey(color)) map[color]?.add(shape)
            else map[color] = listOf(shape).toMutableList()
        }
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShapeCollector

        return shapesList.sortedBy { it.hashCode() } == other.shapesList.sortedBy { it.hashCode() } // compare sorted lists
    }

    override fun hashCode(): Int {
        return shapesList.sortedBy { it.hashCode() }.hashCode() // hash code of sorted list
    }
}