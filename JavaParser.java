
			if(brackets.isEmpty()){
				if(lines.contains("public")||lines.contains("private")||lines.contains("protected")){
					if(lines.next!=null && brackets.openBracket()){
						Linkedlist<String> line = new <String>LinkedList();
						brackets.push("{");
					}
				}
			}
			else{
				list.add(line);
				if(line.contains("{")){
					brackets.push("{");
				}
				else if(line.contains("}")){
					brackets.pop();
				}
				if(stack.isEmpty()){
					list.add(line);
				}
			}
		