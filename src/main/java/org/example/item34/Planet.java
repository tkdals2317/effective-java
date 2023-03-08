package org.example.item34;

// 코드 34-2 데이터와 메서드를 갖는 열거타입
public enum Planet {
    MERCURY (3.303e+23, 2.4397e6),
    VENUS   (4.869e+24, 6.0518e6),
    EARTH   (5.976e+24, 6.37814e6),
    MARS    (6.421e+23, 3.3972e6),
    JUPITER (1.9e+27,   7.1492e7),
    SATURN  (5.688e+26, 6.0268e7),
    URANUS  (8.686e+25, 2.5559e7),
    NEPTUNE (1.024e+26, 2.4746e7),
    PLUTO   (1.27e+22,  1.137e6);

    private final double mass;   // 질량
    private final double radius; // 반지름
    private final double surfaceGravity;

    // 중력 상수
    public static final double G = 6.67300E-11;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }
    public double mass()   { return mass; }
    public double radius() { return radius; }

    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity; // F = ma
    }
}

