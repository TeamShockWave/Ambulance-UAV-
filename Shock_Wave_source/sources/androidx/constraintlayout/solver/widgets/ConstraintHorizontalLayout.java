package androidx.constraintlayout.solver.widgets;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer {
    private ContentAlignment mAlignment = ContentAlignment.MIDDLE;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public ConstraintHorizontalLayout() {
    }

    public ConstraintHorizontalLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ConstraintHorizontalLayout(int width, int height) {
        super(width, height);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r15) {
        /*
            r14 = this;
            r4 = 0
            java.util.ArrayList r1 = r14.mChildren
            int r1 = r1.size()
            if (r1 == 0) goto L_0x0066
            r2 = r14
            r12 = 0
            java.util.ArrayList r1 = r14.mChildren
            int r13 = r1.size()
        L_0x0011:
            if (r12 >= r13) goto L_0x004f
            java.util.ArrayList r1 = r14.mChildren
            java.lang.Object r0 = r1.get(r12)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r0
            if (r2 == r14) goto L_0x003d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            r0.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r1, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r2, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r3)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            r2.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r1, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r0, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r3)
        L_0x002b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            r0.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r1, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r14, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r3)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            r0.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r1, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r14, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r3)
            r2 = r0
            int r12 = r12 + 1
            goto L_0x0011
        L_0x003d:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Strength r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength.STRONG
            androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r1 = r14.mAlignment
            androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r3 = androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout.ContentAlignment.END
            if (r1 != r3) goto L_0x0047
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Strength r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength.WEAK
        L_0x0047:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            r0.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r1, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r2, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r3, (int) r4, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength) r5)
            goto L_0x002b
        L_0x004f:
            if (r2 == r14) goto L_0x0066
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Strength r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength.STRONG
            androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r1 = r14.mAlignment
            androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r3 = androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout.ContentAlignment.BEGIN
            if (r1 != r3) goto L_0x005b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Strength r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength.WEAK
        L_0x005b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r7 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r9 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            r6 = r2
            r8 = r14
            r10 = r4
            r11 = r5
            r6.connect((androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r7, (androidx.constraintlayout.solver.widgets.ConstraintWidget) r8, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type) r9, (int) r10, (androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength) r11)
        L_0x0066:
            super.addToSolver(r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout.addToSolver(androidx.constraintlayout.solver.LinearSystem):void");
    }
}
