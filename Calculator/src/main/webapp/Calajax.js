var txt = "";

$(function () {
		
    $('button').click(function () {

        $('#addValue').focus();

        var inputData = $(this).text();

        if (inputData == "=") {
			AjaxConPost();

        } else if (txt == "") { // 처음 입력
            if (!$.isNumeric(inputData)) { // 첫 글자가 숫자가 아니면 입력 X
                if (inputData == "." || inputData == "(" || inputData == "-") { //., 괄호등은 처음 입력 가능하게 예외
                    txt += inputData;
                    $('#addValue').val(txt);
                }
            } else { // 첫 글자가 숫자면 입력 가능
                txt += inputData;
                $('#addValue').val(txt);
            }
        } else { // 첫 글자가 쓰이고 난 후
            var temp = txt.substr(-1); //마지막 글자

            if (temp == ")") { // 닫는 괄호 뒤에는 연산자가 오도록
                if (!$.isNumeric(inputData)) {
                    txt += inputData;
                    $('#addValue').val(txt);
                }
            } else if (temp == "(") { //여는 괄호 뒤에는 숫자, 여는 괄호, - 만 오게
                if ($.isNumeric(inputData) || inputData == "(" || inputData == "←" || inputData == "-") {
                    txt += inputData;
                    $('#addValue').val(txt);
                }
            } else if ($.isNumeric(temp)) { // 마지막 글자에 숫자면 무난하게 진행
                txt += inputData;
                $('#addValue').val(txt);
            } else { // 마지막 글자가 괄호와 숫자가 아니면(연산자 이면)
                if ($.isNumeric(inputData) || inputData == "(" || inputData == "←" || inputData == "-") { //새로 입력한 글자가 숫자와 괄호면 무난하게 진행
                    txt += inputData;
                    $('#addValue').val(txt);
                } else { //연산자이면 새로운 연산자로 교체
                    txt = txt.slice(0, -1); //마지막 글자를 지우고
                    txt += inputData; //txt에 새로운 글저 추가
                    $('#addValue').val(txt); //txt 출력
                }
            }
        }

        //특수 처리
        if (inputData == "←") { // 지움키가 나오면 마지막 글자를 지움
            txt = txt.slice(0, -2);
            $('#addValue').val(txt);
        } else if (inputData == "C") { //전체삭제
            txt = "";
            $('#addValue').val(txt);
        } else if (inputData == "(") { //괄호 변경
            $('#bracket').text(")");
        } else if (inputData == ")") {
            $('#bracket').text("(");
        }
    });
});