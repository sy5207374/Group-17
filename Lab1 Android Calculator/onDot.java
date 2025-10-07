private void onDot() {
        if (justEvaluated && pendingOp == null) {
            clearAll();
        }
        if (input.indexOf(".") >= 0) return;      // 已有小数点
        if (input.length() == 0) input.append("0"); // 直接输入 "." 时补 0
        input.append(".");
        updateDisplay(input.toString());
        justEvaluated = false;
    }

    private void onOperator(char op) {
        // 若没有正在输入的数字，但想连按运算符：替换运算符
        if (input.length() == 0 && accumulator != null) {
            pendingOp = op;
            return;
        }

        double current = parseInputAsDouble();
        if (accumulator == null) {
            accumulator = current;      // 第一个数
        } else if (pendingOp != null) {
            Double result = apply(accumulator, current, pendingOp);
            if (result == null) { // 错误（如除 0）
                showError();
                return;
            }
            accumulator = result;
            updateDisplay(format(result));
        }
        pendingOp = op;
        input.setLength(0); // 准备输入下一个操作数
        justEvaluated = false;
    }

    private void onEquals() {
        if (pendingOp == null) {
            // 没有待运算的操作，保持当前显示
            return;
        }
        double current = parseInputAsDouble();
        Double result = apply(accumulator == null ? 0.0 : accumulator, current, pendingOp);
        if (result == null) {
            showError();
            return;
        }
        updateDisplay(format(result));
        // 结果作为新的起点
        accumulator = result;
        pendingOp = null;
        input.setLength(0);
        justEvaluated = true;
    }

    private void backspace() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            updateDisplay(input.length() == 0 ? "0" : input.toString());
        }
    }

    private void clearAll() {
        input.setLength(0);
        accumulator = null;
        pendingOp = null;
        justEvaluated = false;
        updateDisplay("0");
    }

    private double parseInputAsDouble() {
        if (input.length() == 0) return 0.0;
        try {
            return Double.parseDouble(input.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    /** 执行二元运算；除以 0 返回 null 表示错误 */
    private Double apply(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0.0) return null;
                return a / b;
        }
        return null;
    }

    private void showError() {
        clearAll();
        updateDisplay("Error");
        justEvaluated = true;
    }

    private void updateDisplay(String s) {
        tv.setText(s);
    }

    /** 去掉尾部 .0 的简洁格式 */
    private String format(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-12) {
            return String.valueOf((long) Math.rint(v));
        }
        // 控制长度，避免太长
        String s = String.valueOf(v);
        if (s.length() > 16) s = String.format("%.10g", v);
        return s;
    }
