class SegTreeLazy {

    double[] tree;
    double[] lazy;
    int n;

    SegTreeLazy(int n) {

        this.n = n;

        tree = new double[4 * n];
        lazy = new double[4 * n];
    }

    // Build Tree
    void build(int node, int lo, int hi) {

        if (lo == hi) {
            tree[node] = 1.0;
            return;
        }

        int mid = (lo + hi) / 2;

        build(2 * node, lo, mid);
        build(2 * node + 1, mid + 1, hi);

        tree[node] = Math.max(tree[2 * node],
                              tree[2 * node + 1]);
    }

    // Push Lazy Values
    void pushDown(int node) {

        if (lazy[node] != 0) {

            tree[2 * node] += lazy[node];
            lazy[2 * node] += lazy[node];

            tree[2 * node + 1] += lazy[node];
            lazy[2 * node + 1] += lazy[node];

            lazy[node] = 0;
        }
    }

    // Range Update
    void updateRange(int node, int lo, int hi,
                     int l, int r, double delta) {

        // No Overlap
        if (r < lo || hi < l)
            return;

        // Full Overlap
        if (l <= lo && hi <= r) {

            tree[node] += delta;
            lazy[node] += delta;

            return;
        }

        // Partial Overlap
        pushDown(node);

        int mid = (lo + hi) / 2;

        updateRange(2 * node, lo, mid,
                    l, r, delta);

        updateRange(2 * node + 1, mid + 1, hi,
                    l, r, delta);

        tree[node] = Math.max(tree[2 * node],
                              tree[2 * node + 1]);
    }

    // Range Maximum Query
    double queryMax(int node, int lo, int hi,
                    int l, int r) {

        // No Overlap
        if (r < lo || hi < l)
            return Double.MIN_VALUE;

        // Full Overlap
        if (l <= lo && hi <= r)
            return tree[node];

        pushDown(node);

        int mid = (lo + hi) / 2;

        double left = queryMax(2 * node, lo, mid,
                               l, r);

        double right = queryMax(2 * node + 1,
                                mid + 1, hi,
                                l, r);

        return Math.max(left, right);
    }

    public static void main(String[] args) {

        int n = 16;

        SegTreeLazy st = new SegTreeLazy(n);

        st.build(1, 0, n - 1);

        // update [3,9] += 0.5
        st.updateRange(1, 0, n - 1,
                       3, 9, 0.5);

        // update [7,14] += 0.3
        st.updateRange(1, 0, n - 1,
                       7, 14, 0.3);

        // query max [0,15]
        double ans1 = st.queryMax(1, 0, n - 1,
                                  0, 15);

        System.out.println("Max Surge [0,15] = " + ans1);

        // update [2,6] += 0.7
        st.updateRange(1, 0, n - 1,
                       2, 6, 0.7);

        // query max [4,10]
        double ans2 = st.queryMax(1, 0, n - 1,
                                  4, 10);

        System.out.println("Max Surge [4,10] = " + ans2);
    }
}