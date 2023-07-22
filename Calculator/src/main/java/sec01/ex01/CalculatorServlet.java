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

@WebServlet("/calc")
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
		boolean minus = false;// 음수입력 처리
		boolean mminus = false;// 음수입력 처리

		for (int i = 0; i < count; i++) {
			String newBie = st.nextToken();

			if (newBie.equals("+") || newBie.equals("-") || newBie.equals("*") || newBie.equals("/")
					|| newBie.equals("%") || newBie.equals("(") || newBie.equals(")")) {
				int nbLevel = opOrder(newBie);
				System.out.println("신규 연산자 : " + newBie);
				String top;
				if (stack.size() == 0 && list.size() == 0 && newBie.equals("-")) {// 첫 숫자가 음수일 때
					mminus = true;
					continue;
				} else if (newBie.equals("(")) {
					stack.push(newBie);
					minus = true;
				} else if (stack.size() == 0) {
					stack.push(newBie);
					minus = true;
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
					if (newBie.equals("-") && minus == true) {// 음수입력 처리
						mminus = true;
						continue;
					} else if (nbLevel <= topLevel) {
						while (true) {
							if (stack.size() == 0) {
								break;
							}
							top = (String) stack.peek();
							topLevel = opOrder((String) stack.peek());
							if (nbLevel > topLevel) {
								break;
							} else {
								System.out.println("연산자 확인" + stack.peek());
								list.add(stack.pop());
								minus = true;
							}
						}
					}
					stack.push(newBie);
				}
				System.out.println("연산자 입력후 리스트" + list);
				System.out.println("연산자 입력후 스택" + stack);
			} else {// 숫자 입력
				if (mminus == true) {// 음수입력 처리
					list.add(Double.parseDouble(newBie) * -1);
					minus = false;
					mminus = false;
					System.out.println("신규 숫자 :" + Double.parseDouble(newBie) * -1);
				} else {
					list.add(Double.parseDouble(newBie));
					minus = false;
					mminus = false;
					System.out.println("신규 숫자 :" + newBie);
				}
				System.out.println("숫자 입력후 리스트" + list);
				System.out.println("숫자 입력후 스택" + stack);
			}
		}
		while (true) {
			if (stack.size() == 0) {
				break;
			}
			list.add(stack.pop());
		}
		System.out.println("최종 리스트 : " + list);
		System.out.println("최종 스택 : " + stack);
		return list;
	}

	private double postfixCalc(List list) {
		Stack stack = new Stack();
		double result = 0;
		if (list.size() == 1) {
			result = (double) list.get(0);
		} else {
			while (true) {
				if (list.size() == 0) {
					break;
				}
				if (list.get(0).equals("+") || list.get(0).equals("-") || list.get(0).equals("*")
						|| list.get(0).equals("/") || list.get(0).equals("%")) {
					double a = (double) stack.pop();
					System.out.println(a);
					double b = (double) stack.pop();
					System.out.println(b);
					if (list.get(0).equals("+")) {
						result = b + a;
					} else if (list.get(0).equals("-")) {
						result = b - a;
					} else if (list.get(0).equals("*")) {
						result = b * a;
					} else if (list.get(0).equals("/")) {
						result = b / a;
					} else if (list.get(0).equals("%")) {
						result = b % a;
					}
					stack.push(result);
				} else {
					stack.push(list.get(0));
				}
				list.remove(0);
			}
		}
		return (double) result;
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		String value = (String) request.getParameter("value");
		PrintWriter witer = response.getWriter();
		System.out.println("작동확인");
		double result = postfixCalc(postfixOper(value));
		if (result % 1 == 0) {
			witer.print((long) postfixCalc(postfixOper(value)));
		} else {
			witer.print(postfixCalc(postfixOper(value)));
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}
}
