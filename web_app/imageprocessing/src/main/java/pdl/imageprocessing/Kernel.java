package pdl.imageprocessing;

enum Kernels {
    SOBELH(new Kernel(new int[][] {
            { -1, -2, -1 },
            { 0, 0, 0 },
            { 1, 2, 1 }
    })),
    SOBELV(new Kernel(new int[][] {
            { -1, 0, 1 },
            { -2, 0, 2 },
            { -1, 0, 1 }
    })),
    RIDGE(new Kernel(new int[][] {
            { -1, -1, -1 },
            { -1, 8, -1 },
            { -1, -1, -1 }
    })),
    MEAN(new Kernel(new int[][] {
            { 1, 1, 1 },
            { 1, 1, 1 },
            { 1, 1, 1 }
    }, 1f / 9f)),
    GAUSIAN3(new Kernel(new int[][] {
            { 1, 2, 1 },
            { 2, 4, 2 },
            { 1, 2, 1 }
    }, 1f / 16f)),
    GAUSIAN5(new Kernel(new int[][] {
            { 1, 4, 6, 4, 1 },
            { 4, 16, 24, 16, 4 },
            { 6, 24, 36, 24, 6 },
            { 4, 16, 24, 16, 4 },
            { 1, 4, 6, 4, 1 },
    }, 1f / 256f));

    public final Kernel k;

    private Kernels(Kernel k) {
        this.k = k;
    }
};

public class Kernel {

    protected int[][] k;
    protected float nfactor;

    public Kernel(int[][] k) {
        this.k = k;
        this.nfactor = 1f;
    }

    public Kernel(int[][] k, float nfactor) {
        this.k = k;
        this.nfactor = nfactor;
    }

}

