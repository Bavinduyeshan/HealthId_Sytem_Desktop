# qr_scanner.py
import cv2
from pyzbar import pyzbar

def scan_qr_code():
    try:
        cap = cv2.VideoCapture(0)
        if not cap.isOpened():
            print("ERROR:WEBCAM_UNAVAILABLE")
            return

        while True:
            ret, frame = cap.read()
            if not ret:
                print("ERROR:CAPTURE_FAILED")
                break

            decoded_objects = pyzbar.decode(frame)
            for obj in decoded_objects:
                student_id = obj.data.decode('utf-8')
                cap.release()
                cv2.destroyAllWindows()
                print(student_id)  # Only stdout output
                return

            cv2.imshow("QR Code Scanner", frame)
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        cap.release()
        cv2.destroyAllWindows()
        print("ERROR:NO_QR_DETECTED")

    except Exception as e:
        print(f"ERROR:{str(e)}")

if __name__ == "__main__":
    scan_qr_code()