import random

NUM_STUDENTS = 200
NUM_PROJECTS = 10
MIN_NOTES = 80

def generate_files():
    for i in range(1, NUM_PROJECTS + 1):
        filename = f"proiect{i}.txt"
        num_notes = random.randint(MIN_NOTES, NUM_STUDENTS)

        with open(filename, "w") as f:
            for _ in range(num_notes):
                student_id = random.randint(1, NUM_STUDENTS)
                grade = random.randint(0, 10)
                f.write(f"({student_id}, {grade})\n")

        print(f"Generated {filename} with {num_notes} lines.")

if __name__ == "__main__":
    generate_files()

