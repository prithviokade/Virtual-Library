for i in range(2, 11):
    print(f"Button btn{i}")

for i in range(2, 11):
    print(f"btn{i} = binding.btn{i};")

for i in range(2, 11):
    print(f"btn{i}.setOnClickListener(new View.OnClickListener(){{\n\t@Override\n\tpublic void onClick(View view) {{\n\t\tsize = {i};\n\t}}\n}});")
