package sec01.ex01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/clac")
public class CalculatorServlet extends HttpServlet {

	static int opOrder(String s) {
		switch (s) {
		case "+":
		case "-":
			return 1;
		case "*":
		case "/":
			return 2;
		case "%":
			return 3;
		default:
			return -1;
		}
	}

	private List postfixOper(String value) {

		StringTokenizer st = new StringTokenizer(value, "+-*/%()", true);

		int count = st.countTokens();

		ArrayList list = new ArrayList();
		Stack stack = new Stack<>();
		for (int i = 0; i < count; i++) {
			String newBie = st.nextToken();

			if (newBie.equals("+") || newBie.equals("-") || newBie.equals("*") || newBie.equals("/")
					|| newBie.equals("%") || newBie.equals("(") || newBie.equals(")")) {
				int nbLevel = opOrder(newBie);
				String top;
				if (stack.size() == 0) {
					stack.push(newBie);
				} else if (newBie.equals(")")) {
					while (true) {
						if (stack.peek().equals("(")) {
							stack.pop();
							break;
						}
						list.add(stack.pop());
					}
				} else {
					top = (String) stack.peek();
					int topLevel = opOrder(top);
					if (newBie.equals("(")) {
					} else if (nbLevel <= topLevel) {
						while (true) {
							if (stack.size() == 0) {
								break;
							}
							top = (String) stack.peek();
							topLevel = opOrder(top);
							if (nbLevel > topLevel) {
								break;
							} else {
								list.add(stack.pop());
							}
						}
					}
					stack.push(newBie);
				}
			} else {
				list.add(Double.parseDouble(newBie));
			}
		}
		while (true) {
			if (stack.size() == 0) {
				break;
			}
			list.add(stack.pop());
		}
		return list;
	}

	private double postfixCalc(List list) {
		Stack stack = new Stack();
		double result = 0;
		if (list.size() == 1){
			result = (double) list.get(0);
		}else {
			while (true) {
				if (list.size() == 0) {
					break;
				}
				if (list.get(0).equals("+") || list.get(0).equals("-") || list.get(0).equals("*") || 
						list.get(0).equals("/") || list.get(0).equals("%")) {
					double a = (double) stack.pop();
					double b = (double) stack.pop();
					if(list.get(0).equals("+")){
						result = b + a;
					}else if(list.get(0).equals("-")){
						result = b - a;
					}else if(list.get(0).equals("*")){
						result = b * a;
					}else if(list.get(0).equals("/")){
						result = b / a;
					}else if(list.get(0).equals("%")){
						result = b % a;
					}
					stack.push(result);
				}
				 else {
					stack.push(list.get(0));
				}
				list.remove(0);
			}
		}
//		if(result >= 9223372036854775807.0) {
//			throw new ArithmeticException("범위는 9223372036854775807를 넘어갈 수 없습니다!");			
//		}
		return (double) result;
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		String value = (String) request.getParameter("value");
		PrintWriter witer = response.getWriter();

		double result = postfixCalc(postfixOper(value));
		if (result % 1 == 0) {
			witer.print((long) postfixCalc(postfixOper(value)));
		} else {
			witer.print(postfixCalc(postfixOper(value)));
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {doHandle(request, response);}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {doHandle(request, response);}
}
