package by.bsuir.mrz;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class Matrix {
    public final double[][] m;


    public Matrix(int height, int width) {
        m = new double[height][width];
    }

    public Matrix(double[][] m) {
        this.m = m;
    }

    public Matrix(List<Double> m) {
        var result = new double[1][m.size()];
        for (int i = 0; i < m.size(); i++) {
            result[0][i] = m.get(i);
        }
        this.m = result;
    }

    public Matrix transpose() {
        var matrix = new Matrix(this.m[0].length, m.length);
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < this.m[0].length; j++)
                matrix.m[j][i] = m[i][j];
        return matrix;
    }

    public Matrix multiplyMatrix(Matrix b) {
        var result = new Matrix(m.length, b.m[0].length);
        var columnWidth = new double[this.m[0].length];
        for (var j = 0; j < b.m[0].length; j++) {
            for (var k = 0; k < this.m[0].length; k++) {
                columnWidth[k] = b.m[k][j];
            }
            for (var i = 0; i < m.length; i++) {
                var rowWidth = m[i];
                var s = 0.0;
                for (var k = 0; k < this.m[0].length; k++) {
                    s += rowWidth[k] * columnWidth[k];
                    if (Double.isInfinite(s)) {
                        s = s > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                    }
                }
                result.m[i][j] = s;
            }
        }
        return result;
    }

    public Matrix multiplyMatrix(double s) {
        var result = new Matrix(m.length, this.m[0].length);
        var c = result.m;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < this.m[0].length; j++) {
                c[i][j] = s * m[i][j];
                if (Double.isInfinite(c[i][j])) {
                    c[i][j] = c[i][j] > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                }
            }
        }
        return result;
    }

    public Matrix addMatrix(Matrix b) {
        var result = new Matrix(m.length, this.m[0].length);
        var c = result.m;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < this.m[0].length; j++) {
                c[i][j] = m[i][j] + b.m[i][j];
                if (Double.isInfinite(c[i][j])) {
                    c[i][j] = c[i][j] > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                }
            }
        }
        return result;
    }

    public Matrix copy() {
        return new Matrix(m);
    }

    public Matrix activateMatrix(DoubleUnaryOperator function) {
        var result = new Matrix(m.length, this.m[0].length);
        var c = result.m;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < this.m[0].length; j++) {
                c[i][j] = function.applyAsDouble(m[i][j]);

                if (Double.isInfinite(c[i][j]))
                    c[i][j] = c[i][j] > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
            }
        }
        return result;
    }

    public void ogDiagonal() {
        var mat = new Matrix(this.m[0].length, this.m.length);
        for (int i = 0; i < this.m.length; i++)
            for (int j = 0; j < this.m[0].length; j++)
                if (i==j) mat.m[j][i] = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix matrix)) {
            return false;
        }

        return Arrays.deepEquals(m, matrix.m);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(m);
    }
}